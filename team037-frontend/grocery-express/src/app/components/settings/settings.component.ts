import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { SettingsService } from 'src/app/settings.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  dataToArchiveForm!: FormGroup;

  constructor(private settingsService: SettingsService) { }

  ngOnInit(): void {
  }

  reloadpage() {
    window.location.reload();
  }
}
