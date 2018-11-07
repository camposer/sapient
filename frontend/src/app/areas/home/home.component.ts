import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

import { catchError } from "rxjs/operators";

import { AppInfoService } from "../../shared";
import { CardService } from "../../services/card.service";
import { Card } from "../../models/card.model";
import { EMPTY } from "rxjs";

@Component({
	selector: "app-home",
	templateUrl: "./home.component.html",
	styleUrls: ["./home.component.scss"],
})
export class HomeComponent implements OnInit {
	title = this.appInfo.title;
	cardForm!: FormGroup;
	serverErrors = [];
    submitted = false;	
	cards = [];

	constructor(
		private appInfo: AppInfoService, 
		private cardService: CardService,
		private formBuilder: FormBuilder
	) {}

	ngOnInit(): void {
		this.loadCards(	);
		this.cardForm = this.formBuilder.group({
            number: ['', [Validators.required, Validators.pattern("[0-9]*"), Validators.maxLength(19)]],
            holderName: ['', Validators.required],
            limit: ['', [Validators.required, Validators.pattern("[0-9]*")]]
        });
	}

    get f() { return this.cardForm.controls; }	

    onSubmit() {
		this.submitted = true;
		this.serverErrors = [];

        if (this.cardForm.invalid) {
            return;
        }

        this.addCard(this.cardForm.getRawValue());
    }	

	private loadCards() {
		this.cardService.getCards().subscribe((cards: any) => this.cards = cards);
	}

	private addCard(card: Card): void {
		this.cardService.addCard(card)
			.pipe(
				catchError(err => {
					if (err.status == 400) {
						this.serverErrors = err.error.errors || [];
					} else if (err.status == 500) {
						this.serverErrors = [ { defaultMessage: err.error.message || '' } ] as any;
					}
					return EMPTY;
				})
			).subscribe(() => this.loadCards());
	}
}
