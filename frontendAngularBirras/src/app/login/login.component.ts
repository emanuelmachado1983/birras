import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ApiService } from '../api.service';
import { MyErrorStateMatcher } from '../common/validators/error-state-matcher';

export function TRANSLATE(str: string) {
  return str;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  matcher = new MyErrorStateMatcher();

  constructor(private router: Router,
    private fb: FormBuilder,
    private apiService: ApiService,
    private translate: TranslateService
    ) { 
      
    this.form = this.fb.group({
      user: ['', Validators.required,],
      password: ['', [Validators.required]]
    });
    this.form.valueChanges.subscribe(_ => {
      const controllersToCheck = [
        'user',
        'password'
      ];

    });
  }

  get user() { return this.form.get("user") };
  get password() { return this.form.get("password") };

  ngOnInit(): void {
    
  }

  goToChangePassword() {
    this.router.navigate(["changePassword"]);
  }

  goToRetrieveUser() {
    this.router.navigate(["retrieveUser"]);
  }

  goToRegistration() {
    this.router.navigate(["registration"]);
  }
  
  onSubmit(): void {
    if (this.form.valid) {
      this.apiService.login(this.user.value, this.password.value).subscribe(
        res  => {
          let result:any = res;
          localStorage.setItem('token', result.token);
          this.router.navigate(["meetups"]);
          
        },
        error  => {
          this.translate.get('errorLogin').subscribe(res => { alert (res); });
          
        }
      );
    } 
  }

}
