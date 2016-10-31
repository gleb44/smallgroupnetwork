import { Route } from '@angular/router';
import { AboutComponent } from './index';

export const AboutRoutes: Route[] = [
  <Route>{
    path: 'about',
    component: AboutComponent
  }
];
