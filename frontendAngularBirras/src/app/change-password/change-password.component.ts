import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ApiService } from '../api.service';
import { MyErrorStateMatcher } from '../common/validators/error-state-matcher';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {
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

  get user() { return this.form.get('user') };
  get password() { return this.form.get('password') };

  ngOnInit(): void {
  }
  
  goToLogin() {
    this.router.navigate(["login"]);
  }
  
  onSubmit(): void {
    console.log("ema");
    if (this.form.valid) {
      this.apiService.patchLogin(this.user.value, this.password.value).subscribe(
        result  => {
          alert ('Modificaste la pass con éxito!!');
          this.router.navigate(["login"]);
        },
        error  => {
          console.log(error);
          if (error!= null) {
            //alert ('Error en la modificación de pasword!!');
            this.translate.get('errorOperacion').subscribe(res => { alert (res); });
          }
          
        }
      );
    } 
  }

}
