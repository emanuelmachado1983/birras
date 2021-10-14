import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';


@Injectable({
    providedIn: 'root'
})

export class ApiService {

    constructor(private httpClient: HttpClient) { }

    private baseUrl = '/api';

    login(user: string, password: string) {
        const headers = new HttpHeaders()
            .set('Authorization', 'Basic xpto')
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
        }

        let body = {
            "userBeer": user,
            "password": password
        }

        return this.httpClient.post(this.baseUrl + '/login/'
            , body, opciones
        );
    }

    patchLogin(user: string, password: string) {
        const headers = new HttpHeaders()
            .set('Authorization', 'Basic xpto')
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
        }

        let body = {
            "userBeer": user,
            "password": password
        }

        return this.httpClient.patch(this.baseUrl + '/login/'
            , body, opciones
        );
    }


    postRegistrar(user: string, email:string, name: string, password: string) {
        const headers = new HttpHeaders()
            .set('Authorization', 'Basic xpto')
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
        }

        let body = {
            "userBeer": user,
            "email": email,
            "name": name,
            "password": password
        }

        return this.httpClient.post(this.baseUrl + '/users/'
            , body, opciones
        );
    }

    retrieveUserByEmail(email: string) {

        const headers = new HttpHeaders()
            .set('Authorization', 'Basic xpto')
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }

        return this.httpClient.get(this.baseUrl + '/users/retrieveUserByEmail/' + email,
            opciones
        );
    }

    getMyUser() {
        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }

        return this.httpClient.get(this.baseUrl + '/users/getMyUser/',
            opciones
        );
    }


    postNewMeetup(name: string, dateFrom:string, hours: string, address: string) {
        let dateParts: string[]= dateFrom.split("/");
        let day: string = dateParts[0];
        let month: string = dateParts[1];
        let year: string = dateParts[2].split(" ")[0];
        let partHour: string = dateParts[2].split(" ")[1];
        let dateFinal: string = year + "-" + month +"-" + day + "T" + partHour;

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
        }

        let body = {
            "name": name,
            "dateFrom": dateFinal,
            "hours": hours,
            "address": address
        }

        return this.httpClient.post(this.baseUrl + '/meetups/'
            , body, opciones
        );
    }

    patchMeetup(id: string, name: string, dateFrom:string, hours: string, address: string) {
        let dateParts: string[]= dateFrom.split("/");
        let day: string = dateParts[0];
        let month: string = dateParts[1];
        let year: string = dateParts[2].split(" ")[0];
        let partHour: string = dateParts[2].split(" ")[1];
        let dateFinal: string = year + "-" + month +"-" + day + "T" + partHour;

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
        }

        let body = {
            "id": id,
            "name": name,
            "dateFrom": dateFinal,
            "hours": hours,
            "address": address
        }

        return this.httpClient.patch(this.baseUrl + '/meetups/'
            , body, opciones
        );
    }



    getMeetUpsIam() {

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }

        return this.httpClient.get(this.baseUrl + '/meetupsUsers/whereIam/',
            opciones
        );
    }

    getMeetUp(idMeetup) {
        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }

        return this.httpClient.get(this.baseUrl + '/meetups/getOne/' + idMeetup,
            opciones
        );
    }

    getTemperature(idMeetup) {
        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }

        return this.httpClient.get(this.baseUrl + '/meetups/temperature/' + idMeetup,
            opciones
        );
    }

    deleteMeFromMeetUp(idMeetup) {

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }

        return this.httpClient.delete(this.baseUrl + '/meetupsUsers/deleteMe/' + idMeetup,
            opciones
        );
    }

    deleteUserFromMeetUp(idUserMeetup) {

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }

        return this.httpClient.delete(this.baseUrl + '/meetupsUsers/delete/' + idUserMeetup,
            opciones
        );
    }

    addUserToMeetUp(idMeetup, idUser) {

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }
        return this.httpClient.post(this.baseUrl + '/meetupsUsers/add/' + idMeetup + "/" + idUser
            , null, opciones
        );
    }

    addMeToMeetUp(idMeetup) {

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }
        return this.httpClient.post(this.baseUrl + '/meetupsUsers/addMe/' + idMeetup
            , null, opciones
        );
    }

    checkInOutMeetUp(idMeetup) {

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }
        return this.httpClient.post(this.baseUrl + '/meetupsUsers/changeCheckIn/' + idMeetup
            , null, opciones
        );
    }


    getMeetUpsByUser(idMeetup) {
        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
        }

        return this.httpClient.get(this.baseUrl + '/meetupsUsers/' + idMeetup,
            opciones
        );
    }

    getMeetUpsByUserNotInvited(idMeetup) {
        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
        }

        return this.httpClient.get(this.baseUrl + '/meetupsUsers/notInvited/' + idMeetup,
            opciones
        );
    }

    getMeetUpsNotIam() {

        const headers = new HttpHeaders()
            .set('Authorization', localStorage.getItem('token'))
            .set('Content-Type', 'application/json');
        
        const opciones = {
            headers: headers,
            responseType: 'json' as 'text',
            //withCredentials: true
            
        }

        return this.httpClient.get(this.baseUrl + '/meetupsUsers/whereNotIam/',
            opciones
        );
    }


}
