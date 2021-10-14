import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontendBirras';
 

  constructor(private router: Router,
    public translate: TranslateService) { 
      this.translate.addLangs(['es', 'en']);
      this.translate.setDefaultLang('es');
  }

  ngOnInit() {
    
  }

  loggedIn() {
    //TODO: mejorar esto, para que no le pegue todo el tiempo al localstorage
    return localStorage.getItem('token') != null && localStorage.getItem('token') != '';
  }

  logOut() {
    localStorage.setItem('token', '');
    this.router.navigate(["/"]);
  }
}

