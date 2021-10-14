import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ApiService } from '../api.service';
import { MyErrorStateMatcher } from '../common/validators/error-state-matcher';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  form: FormGroup;
  matcher = new MyErrorStateMatcher();

  constructor(private router: Router,
    private fb: FormBuilder,
    private apiService: ApiService,
    private translate: TranslateService
    ) { 
      
    this.form = this.fb.group({
      user: ['', Validators.required,],
      name: ['', Validators.required,],
      email: ['', Validators.required,],
      password: ['', [Validators.required]]
    });
    this.form.valueChanges.subscribe(_ => {
      const controllersToCheck = [
        'user',
        'name',
        'email',
        'password'
      ];

    });
  }

  get user() { return this.form.get('user') };
  get name() { return this.form.get('name') };
  get email() { return this.form.get('email') };
  get password() { return this.form.get('password') };

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.apiService.postRegistrar(this.user.value, this.email.value, this.name.value, this.password.value).subscribe(
        result  => {
          this.translate.get('registroExito').subscribe(res => { alert (res); });
          this.router.navigate(["login"]);
        },
        error  => {
          console.log(error);
          if (error!= null) {
            if (error.error.errors[0] == 'user exists'){
              this.translate.get('existeUsuario').subscribe(res => { alert (res); });
            } else if (error.error.errors[0] == 'email exists'){
              this.translate.get('existeEmail').subscribe(res => { alert (res); });
            } else {
              this.translate.get('errorEnRegistracion').subscribe(res => { alert (res); });
            }
          }
          
        }
      );
    } 
  }
}
