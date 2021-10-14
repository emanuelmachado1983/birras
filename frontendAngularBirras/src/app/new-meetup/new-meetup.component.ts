import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ApiService } from '../api.service';
import { MyErrorStateMatcher } from '../common/validators/error-state-matcher';

@Component({
  selector: 'app-new-meetup',
  templateUrl: './new-meetup.component.html',
  styleUrls: ['./new-meetup.component.scss']
})
export class NewMeetupComponent implements OnInit {


  form: FormGroup;
  matcher = new MyErrorStateMatcher();
  dateControl;
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
  }

  goToMeetups():void {
    this.router.navigate(["meetups"]);
  }

  onSubmit(): void {
    
    if (Number(this.hours.value) > 6) {
      this.translate.get('errorMeetupHoras').subscribe(res => { alert (res); });
      return;
    }

    if (this.form.valid) {
      this.apiService.postNewMeetup(this.name.value, this.dateMeetup.value, this.hours.value, this.address.value).subscribe(
        result  => {
          this.translate.get('meetupModificadaConExito').subscribe(res => { alert (res); });
          this.router.navigate(["meetups"]);
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
