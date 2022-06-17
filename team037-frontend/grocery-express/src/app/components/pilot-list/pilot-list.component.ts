import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Pilot } from 'src/app/common/pilot';
import { PilotService } from 'src/app/services/pilot.service';

@Component({
  selector: 'app-pilot-list',
  templateUrl: './pilot-list.component.html',
  styleUrls: ['./pilot-list.component.css']
})
export class PilotListComponent implements OnInit {
  loggedInType = localStorage.getItem('loggedInType')

  pilots!: Pilot[];
  addPilotForm!: FormGroup;
  errCode = 0;
  errMsg!:string;

  constructor(private pilotService: PilotService, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.listPilots();

    this.addPilotForm = this.fb.group({
      'user_id': new FormControl('', Validators.required),
      'first_name': new FormControl('', Validators.required),
      'last_name': new FormControl('', Validators.required),
      'phone': new FormControl('', Validators.required),
      'tax_id': new FormControl('', Validators.required),
      'license': new FormControl('', Validators.required),
      'experience': new FormControl('', Validators.required)
    });
  }

  listPilots() {
    this.pilotService.getPilotList().subscribe(
      (data) => {
        this.pilots = data;
      }
    )
  }

  addPilot(form: any) {
    
    //console.log(form.value)
    let newPilot = {
      account: form.value.account,
      firstName: form.value.firstName,
      lastName: form.value.lastName,
      phoneNumber: form.value.phoneNumber,
      taxID: form.value.taxID,
      licenseID: form.value.licenseID,
      experience: parseInt(form.value.experience)
    }
    
    console.log(newPilot);
    
    // Add new pilot
    this.pilotService.testPost(newPilot).subscribe(
      (data) => {
        console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    );

    // this.addPilotForm.valueChanges.subscribe(data=>{
    //   console.log(data);
    // })
  }

  resetForm(form: any) {
    form.resetForm();
    this.errCode = 0;
    this.errMsg = '';
    window.location.reload();
  }

}
