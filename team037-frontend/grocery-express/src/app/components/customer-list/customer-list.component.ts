import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Customer } from 'src/app/common/customer';
import { CustomerArchived } from 'src/app/common/customer-archived';
import { CustomerList } from 'src/app/common/customer-list';
import { CustomerService } from 'src/app/services/customer.service';


@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {
  loggedInType = localStorage.getItem('loggedInType')

  customers!: CustomerList;
  archivedCustomers!: CustomerArchived[];
  archivedCustomers_asc!: CustomerArchived[];
  archivedCustomers_des!: CustomerArchived[];
  currentCustomers!: Customer[];
  currentCustomers_asc!: Customer[];
  currentCustomers_des!: Customer[];
  addCustomerForm!: FormGroup;
  errCode = 0;
  errMsg!: string;
  currentCustomer!: string;
  showCurrentData = true;
  showArchiveData = false;
  isSroted = false;
  hideloader = false;
  currentCustomerId!:number;

  constructor(private customerService: CustomerService, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.listCustomers();

    this.addCustomerForm = this.fb.group({
      'name': new FormControl('', Validators.required),
      'phone_number': new FormControl('', Validators.required),
      'rating': new FormControl('', Validators.required),
      'credit': new FormControl('', Validators.required),
      'last_login': new FormControl('', Validators.required)
    });
  }

  listCustomers() {
    this.customerService.getCustomerList().subscribe(
      (data) => {
        if (data) {
          this.hideloader = true;
        }

        console.log(data);
        this.customers = data;
        this.archivedCustomers = data.archivedCustomers;
        this.currentCustomers = data.currentCustomers;

        console.log(data.currentCustomers.sort((a, b) => (a.rating < b.rating) ? 1 : ((b.rating < a.rating) ? -1 : 0)))
        console.log(data.currentCustomers.sort((a, b) => (a.rating > b.rating) ? 1 : ((b.rating > a.rating) ? -1 : 0)))

        this.archivedCustomers_asc = data.archivedCustomers.sort((a, b) => a.rating - b.rating);
        this.archivedCustomers_des = data.archivedCustomers.sort((a, b) => b.rating - a.rating);

        this.currentCustomers_asc = data.currentCustomers.sort((a, b) => a.rating - b.rating);
        this.currentCustomers_des = data.currentCustomers.sort((a, b) => b.rating - a.rating);

      }
    )
  }

  sortCurrent() {
    if (this.isSroted) {
      let newArr = this.currentCustomers.sort((a, b) => a.rating - b.rating);
      this.currentCustomers = newArr;
    } else {
      let newArr = this.currentCustomers.sort((a, b) => b.rating - a.rating)
      this.currentCustomers = newArr;
    }

    this.isSroted = !this.isSroted;
  }

  sortArchive() {
    if (this.isSroted) {
      let newArr = this.archivedCustomers.sort((a, b) => a.rating - b.rating);
      this.archivedCustomers = newArr;
    } else {
      let newArr = this.archivedCustomers.sort((a, b) => b.rating - a.rating)
      this.archivedCustomers = newArr;
    }

    this.isSroted = !this.isSroted;
  }

  addCustomer(form: any) {
    //console.log(form.value)
    let newCustomer = {
      username: form.value.username,
      firstName: form.value.firstName,
      lastName: form.value.lastName,
      phoneNumber: form.value.phoneNumber,
      rating: form.value.rating,
      credit: parseInt(form.value.credit)
    }

    //console.log(newCustomer);
    this.customerService.addCustomer(newCustomer).subscribe(
      (data) => {
        console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    );

    if (this.errCode == 200) {
      form.resetForm();
    }

    // this.addPilotForm.valueChanges.subscribe(data=>{
    //   console.log(data);
    // })
  }

  setCurrentCustomer(currentCustomer: string) {
    this.currentCustomer = currentCustomer;
  }

  setCurrentCustomerId(currentCustomerId: number) {
    this.currentCustomerId = currentCustomerId;
  }

  
  changeRating(form: any) {
    this.errCode = 0;
    this.errMsg = '';

    console.log(this.currentCustomerId);

    //console.log(form.value)
    let newRating = {
      userId: this.currentCustomerId,
      manualRating: form.value.manualRating
    }

    //console.log(newCustomer);
    this.customerService.changeRatingService(newRating).subscribe(
      (data) => {
        console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    );

    if (this.errCode != 200) {
      console.log('wrong');
      this.errCode = 500;
    }

    if (this.errCode == 200) {
      form.resetForm();
    }

  }

  //TODO: Update current/archived data
  updateView(form: any) {
    let newView = {
      viewCurrent: form.value.currentData,
      viewArchived: form.value.archivedData
    }

    // TODO
    this.customerService.updateDataView(newView).subscribe(data => {
      console.log(data);
      this.errCode = data.code;
      this.customers = data.customers;
    });

  }

  updatewindow() {
    window.location.reload();
  }

  test(thisRating: string) {
    console.log(thisRating);
  }

  resetForm(form: any) {
    form.resetForm();
    this.errCode = 0;
    this.errMsg = '';
    window.location.reload();
  }

  // POST Request to change data view
  updateData() {
    window.location.reload();
  }

}
