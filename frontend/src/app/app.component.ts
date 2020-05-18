import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {stringify} from "querystring";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  apiURL: string = 'http://localhost:8181/demo';
  constructor(private httpClient: HttpClient) { }

  public getTestedEndpoint() {
    return this.httpClient.get(`${this.apiURL}/tested`, {responseType: 'text'})
  }
  public getUntestedEndpoint(){
    return this.httpClient.get(`${this.apiURL}/untested`, {responseType: 'text'})
  }

}
