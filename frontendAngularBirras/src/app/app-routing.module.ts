import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdministrateMeetupComponent } from './administrate-meetup/administrate-meetup.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { LoginComponent } from './login/login.component';
import { MeetupsComponent } from './meetups/meetups.component';
import { NewMeetupComponent } from './new-meetup/new-meetup.component';
import { RegistrationComponent } from './registration/registration.component';
import { RetrieveUserComponent } from './retrieve-user/retrieve-user.component';


const routes: Routes = [
  {
    path: '',
    component: LoginComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'registration',
    component: RegistrationComponent
  },
  {
    path: 'changePassword',
    component: ChangePasswordComponent
  },
  {
    path: 'retrieveUser',
    component: RetrieveUserComponent
  },
  {
    path: 'meetups',
    component: MeetupsComponent
  },
  {
    path: 'administrateMeetup',
    component: AdministrateMeetupComponent
  },
  {
    path: 'newMeetup',
    component: NewMeetupComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
