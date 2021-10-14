import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';
import { MyErrorStateMatcher } from '../common/validators/error-state-matcher';

@Component({
  selector: 'app-retrieve-user',
  templateUrl: './retrieve-user.component.html',
  styleUrls: ['./retrieve-user.component.scss']
})
export class RetrieveUserComponent implements OnInit {

  form: FormGroup;
  matcher = new MyErrorStateMatcher();
  user: String;

  constructor(private router: Router,
    private fb: FormBuilder,
    private apiService: ApiService,
    ) { 
      
    this.form = this.fb.group({
      email: ['', Validators.required,]
    });
    this.form.valueChanges.subscribe(_ => {
      const controllersToCheck = [
        'email'
      ];

    });
  }

  get email() { return this.form.get('email') };

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.apiService.retrieveUserByEmail(this.email.value).subscribe(
        result  => {
          let aux:any = result;
          if (aux.foundMail) {
            this.user = 'Tu usuario es: ' + aux.user;
          } else {
            this.user = 'No se encontró el usuario';
          }
        },
        error  => {
          console.log(error);
          if (error!= null) {
            alert ('Error en la búsqueda de usuario!!');
          }
        }
      );
    } 
  }


}
