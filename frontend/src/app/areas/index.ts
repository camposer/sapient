import { Routes } from "@angular/router";

import { NavComponent } from "./nav/nav.component";
import { HomeComponent } from "./home/home.component";
import { ErrorComponent } from "./error/error.component";
import { NotFoundComponent } from "./not-found/not-found.component";

export const AREAS_ROUTES: Routes = [
	{ path: "", component: HomeComponent, pathMatch: "full" },
	{ path: "error", component: ErrorComponent },
	{ path: "**", component: NotFoundComponent },
];

export const AREAS_COMPONENTS = [HomeComponent, NavComponent, ErrorComponent, NotFoundComponent];
