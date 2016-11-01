import { Component } from '@angular/core';
import { MouseEvent } from 'angular2-google-maps/core';

interface Marker {
    lat: number;
    lng: number;
    label?: string;
    draggable?: boolean;
}

/**
 * This class represents the navigation bar component.
 */
@Component({
    moduleId: module.id,
    selector: 'sd-google-maps',
    templateUrl: 'google-maps.component.html',
    styleUrls: ['google-maps.component.css'],
})

export class GoogleMapsComponent {
    // Google Maps zoom level
    zoom: number = 8;
    // Initial center position for the map
    lat: number = 51.673858;
    lng: number = 7.815982;

    markers: Marker[] = [
        {
            lat: 51.673858,
            lng: 7.815982,
            label: 'A',
            draggable: true
        },
        {
            lat: 51.373858,
            lng: 7.215982,
            label: 'B',
            draggable: false
        }
    ];

    clickedMarker(label: string, index: number) {
        console.log(`Clicked the marker: ${label || index}`);
    }

    mapClicked($event: MouseEvent) {
        console.log('AddMarker', $event);
        this.markers.push({
            lat: $event.coords.lat,
            lng: $event.coords.lng
        });
    }

    markerDragEnd(m: Marker, $event: MouseEvent) {
        console.log('DragEnd', m, $event);
    }
}
