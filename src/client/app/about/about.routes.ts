import { Route } from '@angular/router';
import { AboutComponent } from './index';

export const aboutPath = 'about';

export const AboutRoutes: Route[] = [
  <Route>{
    path: aboutPath,
    component: AboutComponent
  }
];
