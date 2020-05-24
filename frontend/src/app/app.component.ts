import {Component, OnInit} from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'frontend';
  apiURL = 'http://localhost:8181/demo';
  responseTested: string;
  responseUntested: string;
  constructor(private httpClient: HttpClient) {

  }

  public getTestedEndpoint() {
    return this.httpClient.get(`${this.apiURL}/tested`, {responseType: 'text'});
  }
  public getUntestedEndpoint(){
    return this.httpClient.get(`${this.apiURL}/untested`, {responseType: 'text'});
  }

  ngOnInit(): void {
    this.getUntestedEndpoint().subscribe(data => {this.responseUntested = data; });
    this.getTestedEndpoint().subscribe(data => {this.responseTested = data; });
  }

}
