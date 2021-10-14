import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { finalize } from 'rxjs/operators';
import { ApiService } from '../api.service';
import { MyErrorStateMatcher } from '../common/validators/error-state-matcher';

@Component({
  selector: 'app-administrate-meetup',
  templateUrl: './administrate-meetup.component.html',
  styleUrls: ['./administrate-meetup.component.scss']
})
export class AdministrateMeetupComponent implements OnInit {


  form: FormGroup;
  matcher = new MyErrorStateMatcher();
  dateControl;
  idMeetup;
  temperature:String;
  quantityBoxBirras: String;
  usersMeetup;
  usersMeetupNotInvited;


  constructor(private router: Router,
    private fb: FormBuilder,
    private apiService: ApiService,
    private translate: TranslateService
    ) { 
      
    this.form = this.fb.group({
      name: ['', Validators.required,],
      address: ['', Validators.required,],
      dateMeetup: ['', Validators.required,],
      hours: ['', [Validators.required]]
    });
    this.form.valueChanges.subscribe(_ => {
      const controllersToCheck = [
        'user',
        'address',
        'dateMeetup',
        'hours'
      ];
    });
  }

  get name() { return this.form.get('name') };
  get address() { return this.form.get('address') };
  get dateMeetup() { return this.form.get('dateMeetup') };
  get hours() { return this.form.get('hours') };

  ngOnInit(): void {
    this.idMeetup = localStorage.getItem('UsingIdMeetup');
    this.traerDatos();
  }

  traerDatos(): void {
    this.temperature = '';
    this.quantityBoxBirras = '';
    //TODO: acá debería ir un forkjoin.
    this.apiService.getMeetUp(this.idMeetup).subscribe(
      result => {
        let res:any = result;
        res.id;
        this.name.setValue(res.name);
        this.address.setValue(res.address);
        this.dateMeetup.setValue(res.date);
        this.hours.setValue(res.hours);
      },
      error  => {
        if (error!= null) {
          this.translate.get('errorTrayendoMeetup').subscribe(res => { alert (res); });
          this.router.navigate(["meetup"]);
        }
      }
    );

    this.apiService.getTemperature(this.idMeetup).subscribe(
      result => {
        let res:any = result;
        this.temperature = res.temperature;
        this.quantityBoxBirras = res.quantityBoxBirras;
      },
      error  => {
        if (error!= null) {
          
        }
      }
    );

    this.apiService.getMeetUpsByUser(this.idMeetup).subscribe(
      result => {
        let res:any = result;
        this.usersMeetup = res;
      },
      error  => {
        if (error!= null) {
        }
      }
    );

    this.apiService.getMeetUpsByUserNotInvited(this.idMeetup).subscribe(
      result => {
        let res:any = result;
        this.usersMeetupNotInvited = res;
      },
      error  => {
        if (error!= null) {
        }
      }
    );
  }

  goToMeetups():void {
    this.router.navigate(["meetups"]);
  }

  deleteUserFromMeetup(idUserMeetup):void {
    this.apiService.deleteUserFromMeetUp(idUserMeetup).pipe(
      finalize(() => {
        this.traerDatos();
      })
    ).subscribe(
      result => {
      },
      error  => {
        if (error!= null) {
          //alert ('Hubo un error al desanotar al usuario!');
          this.translate.get('errorOperacion').subscribe(res => { alert (res); });
        }
      }
    );
  }

  addUserToMeetup(idUser):void {
    this.apiService.addUserToMeetUp(this.idMeetup, idUser).pipe(
      finalize(() => {
        this.traerDatos();
      })
    ).subscribe(
      result => {
      },
      error  => {
        if (error!= null) {
          //alert ('Hubo un error al anotar al usuario!');
          this.translate.get('errorOperacion').subscribe(res => { alert (res); });
        }
      }
    );
  }

  onSubmit(): void {
    
    if (Number(this.hours.value) > 6) {
      this.translate.get('errorMeetupHoras').subscribe(res => { alert (res); });
      return;
    }

    if (this.form.valid) {
      this.apiService.patchMeetup(this.idMeetup, this.name.value, this.dateMeetup.value, this.hours.value, this.address.value).subscribe(
        result  => {
          this.traerDatos();
          this.translate.get('meetupModificadaConExito').subscribe(res => { alert (res); });
        },
        error  => {
          if (error!= null) {
            this.translate.get('errorModificandoMeetup').subscribe(res => { alert (res); });
          }
        }
      );
    }
  }

}
