import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';
import { finalize } from 'rxjs/operators';
import { ɵangular_packages_platform_browser_platform_browser_j } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-meetups',
  templateUrl: './meetups.component.html',
  styleUrls: ['./meetups.component.scss']
})
export class MeetupsComponent implements OnInit {

  constructor(private router: Router,
    private apiService: ApiService,
    private translate: TranslateService) { }

  resIam: any;
  resNotIam: any;
  invitado = false;
  invitadoOld = false;
  administrador = false;
  ngOnInit(): void {
    this.traerDatos();
  }

  traerDatos(): void {
    //acá faltaría hacer un fork por si dan error los dos, así no te tira dos veces el alert.
    this.invitado = false;
    this.invitadoOld = false;
    this.administrador = false;
    this.apiService.getMyUser().subscribe(
      result  => {
        let res:any = result;
        this.administrador = res.admin
      },
      error  => {
      }
    );

    this.apiService.getMeetUpsIam().subscribe(
      result  => {
        this.resIam = result;
        if (this.resIam !=null && this.resIam.length > 0) {
          this.invitadoOld = true;
          for (let register of this.resIam){
            console.log(register);
            if (register.newOne) {
              this.invitado = true;
              break;
            }
          }
        }
      },
      error  => {
        if (error!= null) {
          this.translate.get('errorTrayendoMeetup').subscribe(res => { alert (res); });
          this.router.navigate(["login"]);
        }
      }
    );

    this.apiService.getMeetUpsNotIam().subscribe(
      result  => {
        this.resNotIam = result;
      },
      error  => {
        if (error!= null) {
          //alert ('Hubo un error cuando se trajeron las meets!');
          this.router.navigate(["login"]);
        }
      }
    );
  }

  deleteFromMeetup(id): void {
    this.apiService.deleteMeFromMeetUp(id)
    .pipe(
      finalize(() => {
        this.traerDatos();
      })
    )
    .subscribe(
      result  => {
        this.resIam = result;
      },
      error  => {
        if (error!= null) {
          this.translate.get('errorBorrarMeetup').subscribe(res => { alert (res); });
        }
      }
    );
  }

  addToMeetup(id): void {
    this.apiService.addMeToMeetUp(id)
    .pipe(
      finalize(() => {
        this.traerDatos();
      })
    )
    .subscribe(
      result  => {
        this.resIam = result;
      },
      error  => {
        if (error!= null) {
          this.translate.get('errorAgregarMeetup').subscribe(res => { alert (res); });
          
        }
      }
    );
  }

  chekInOut(id): void {
    this.apiService.checkInOutMeetUp(id)
    .pipe(
      finalize(() => {
        this.traerDatos();
      })
    )
    .subscribe(
      result  => {
        this.resIam = result;
      },
      error  => {
        if (error!= null) {
          this.translate.get('errorOperacion').subscribe(res => { alert (res); });
        }
      }
    );
  }

  goToAdministrateMeetup(id): void {
    localStorage.setItem('UsingIdMeetup', id);
    this.router.navigate(["administrateMeetup"]);
  }

  goToNewMeetup():void {
    this.router.navigate(["newMeetup"]);
  }
  
}
