import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { ServiceWorkerModule } from "@angular/service-worker";
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from "./app-routing.module";

import { environment } from "../environments/environment";
import { AppComponent } from "./app.component";
import { AREAS_COMPONENTS } from "./areas/index";
import { AppSharedModule } from "./shared";
import { CardService } from "./services/card.service";

@NgModule({
	declarations: [AppComponent, ...AREAS_COMPONENTS],
	imports: [
		BrowserModule.withServerTransition({ appId: "serverApp" }),
		AppRoutingModule,
		AppSharedModule,
		HttpClientModule,
		ReactiveFormsModule,
		ServiceWorkerModule.register("/ngsw-worker.js", { enabled: environment.production }),
	],
	providers: [CardService],
	bootstrap: [AppComponent],
})
export class AppModule {}
