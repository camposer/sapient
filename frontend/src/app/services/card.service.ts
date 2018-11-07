import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Card } from '../models/card.model';

@Injectable({
  providedIn: 'root',
})
export class CardService {
	baseUrl = "http://localhost:8080/v1/cards";

    constructor(private http: HttpClient) { }

    getCards() {
		return this.http.get(this.baseUrl);
    }

    addCard(card: Card) {
        return this.http.post(this.baseUrl, card);
    }
}