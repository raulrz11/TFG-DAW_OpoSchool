import { Component } from '@angular/core';
import {ChildrenOutletContexts, RouterOutlet} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {animate, animateChild, group, query, style, transition, trigger} from "@angular/animations";


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoginComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  animations: [
    trigger('AuthRouteAnimations', [
      transition('login => register', [
        style({ position: 'relative' }),
        query(':enter, :leave', [
          style({
            position: 'absolute',
            top: 0,
            transform: 'translateX(0)',
            width: '100%'
          })
        ]),
        query(':enter', [
          style({ transform: 'translateX(100%)' })
        ], { optional: true }),
        query(':leave', animateChild(), { optional: true }),
        group([
          query(':leave', [
            animate('500ms ease-out', style({ transform: 'translateX(-100%)' }))
          ], { optional: true }),
          query(':enter', [
            animate('500ms ease-out', style({ transform: 'translateX(0)' }))
          ], { optional: true }),
        ]),
      ]),
      transition('register => login', [
        style({ position: 'relative' }),
        query(':enter, :leave', [
          style({
            position: 'absolute',
            top: 0,
            transform: 'translateX(0)',
            width: '100%'
          })
        ]),
        query(':enter', [
          style({ transform: 'translateX(-100%)' })
        ], { optional: true }),
        query(':leave', animateChild(), { optional: true }),
        group([
          query(':leave', [
            animate('500ms ease-out', style({ transform: 'translateX(100%)' }))
          ], { optional: true }),
          query(':enter', [
            animate('500ms ease-out', style({ transform: 'translateX(0)' }))
          ], { optional: true }),
        ]),
      ]),
      transition('register => payment', [
        style({ position: 'relative' }),
        query(':enter, :leave', [
          style({
            position: 'absolute',
            top: 0,
            transform: 'translateX(0)',
            width: '100%'
          })
        ]),
        query(':enter', [
          style({ transform: 'translateX(100%)' })
        ], { optional: true }),
        query(':leave', animateChild(), { optional: true }),
        group([
          query(':leave', [
            animate('500ms ease-out', style({ transform: 'translateX(-100%)' }))
          ], { optional: true }),
          query(':enter', [
            animate('500ms ease-out', style({ transform: 'translateX(0)' }))
          ], { optional: true }),
        ]),
      ]),
      transition('payment => register', [
        style({ position: 'relative' }),
        query(':enter, :leave', [
          style({
            position: 'absolute',
            top: 0,
            transform: 'translateX(0)',
            width: '100%'
          })
        ]),
        query(':enter', [
          style({ transform: 'translateX(-100%)' })
        ], { optional: true }),
        query(':leave', animateChild(), { optional: true }),
        group([
          query(':leave', [
            animate('500ms ease-out', style({ transform: 'translateX(100%)' }))
          ], { optional: true }),
          query(':enter', [
            animate('500ms ease-out', style({ transform: 'translateX(0)' }))
          ], { optional: true }),
        ]),
      ]),
    ])
  ]
})
export class AppComponent {
  title = 'AngOposchool';

  constructor(private contexts: ChildrenOutletContexts) {}

  getRouteAnimationData() {
    return this.contexts.getContext('primary')?.route?.snapshot?.data?.['animation'];
  }
}
