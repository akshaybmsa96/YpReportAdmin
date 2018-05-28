package yp.com.akki.ypreportadmin.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterExpandableList;
import yp.com.akki.ypreportadmin.fragment.AccountAddFragment;
import yp.com.akki.ypreportadmin.fragment.AccountEditFragment;
import yp.com.akki.ypreportadmin.fragment.AccountLogFragment;
import yp.com.akki.ypreportadmin.fragment.AccountStatusFragment;
import yp.com.akki.ypreportadmin.fragment.AccountUpdateFragment;
import yp.com.akki.ypreportadmin.fragment.AttendanceEntryFragment;
import yp.com.akki.ypreportadmin.fragment.CentreAddFragment;
import yp.com.akki.ypreportadmin.fragment.CentreEditFragment;
import yp.com.akki.ypreportadmin.fragment.ChooseDateFragment;
import yp.com.akki.ypreportadmin.fragment.StockStatusFragment;
import yp.com.akki.ypreportadmin.fragment.EmployeeAddFragment;
import yp.com.akki.ypreportadmin.fragment.EmployeeAttendanceFragment;
import yp.com.akki.ypreportadmin.fragment.EmployeeEditFragment;
import yp.com.akki.ypreportadmin.fragment.EmployeeStatusFragment;
import yp.com.akki.ypreportadmin.fragment.ExpenseEntryFragment;
import yp.com.akki.ypreportadmin.fragment.ExpenseReportFragment;
import yp.com.akki.ypreportadmin.fragment.ItemAddFragment;
import yp.com.akki.ypreportadmin.fragment.ItemEditFragment;
import yp.com.akki.ypreportadmin.fragment.MaterialDistributionEntryFragment;
import yp.com.akki.ypreportadmin.fragment.MaterialDistributionReportFragment;
import yp.com.akki.ypreportadmin.fragment.PurchaseEntryFragment;
import yp.com.akki.ypreportadmin.fragment.PurchaseReportFragment;
import yp.com.akki.ypreportadmin.fragment.ReceivedPaymentEntryFragment;
import yp.com.akki.ypreportadmin.fragment.ReceivedPaymentReportFragment;
import yp.com.akki.ypreportadmin.fragment.StockAddFragment;
import yp.com.akki.ypreportadmin.fragment.StockEditFragment;
import yp.com.akki.ypreportadmin.fragment.StoreStatusFragment;
import yp.com.akki.ypreportadmin.fragment.VendorAddFragment;
import yp.com.akki.ypreportadmin.fragment.VendorEditFragment;
import yp.com.akki.ypreportadmin.fragment.VendorStatusFragment;
import yp.com.akki.ypreportadmin.global.PreferencedData;


public class Dashboard extends AppCompatActivity {

    private Toolbar tb;
    private ActionBarDrawerToggle toggle;
    private MenuItem prevItem;
    private DrawerLayout drawer;
    private FragmentTransaction fragmentTransaction;
    private boolean doubleBackToExitPressedOnce = false;
    private NavigationView navigationView;
    private ExpandableListView expandableListView;
    private CustomAdapterExpandableList customAdapterExpandableList;

    private List<String> mExpandableListTitle=new ArrayList<>();
    private Map<String, List<String>> mExpandableListData = new HashMap<String, List<String>>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dashboard);


        tb=(Toolbar)findViewById(R.id.toolbar);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setTitle(PreferencedData.getPrefDeliveryCentre(this));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        //expandable list


        expandableListView=(ExpandableListView)findViewById(R.id.navList);

        addItemsToExpandableList();

        customAdapterExpandableList = new CustomAdapterExpandableList(this, mExpandableListTitle, mExpandableListData);
        expandableListView.setAdapter(customAdapterExpandableList);


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

             //   Toast.makeText(Dashboard.this,"Group : " + groupPosition + "Child : "+ childPosition,Toast.LENGTH_SHORT).show();

                if(groupPosition == 0)
                {
                    if(childPosition == 0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new StockStatusFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(PreferencedData.getPrefDeliveryCentre(Dashboard.this));

                    }

                    else if(childPosition == 1)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ChooseDateFragment(),"DateTag");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Date");
                    }

                    else if(childPosition == 2)
                    {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new PurchaseReportFragment(),"Purchase Report");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Purchase Report");
                    }

                    else if(childPosition == 3)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ExpenseReportFragment(),"Expense Report");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Expense Report");
                    }

                    else if(childPosition == 4)
                    {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountLogFragment(),"Account Log");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Account Log");
                    }

                    else if(childPosition == 5)
                    {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountStatusFragment(),"Account Status");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Account Status");
                    }

                    else if(childPosition == 6)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new StoreStatusFragment(),"Stores Status");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Stores Status");
                    }

                    else if(childPosition == 7)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new VendorStatusFragment(),"Vendor Status");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Vendor Status");
                    }

                    else if(childPosition == 8)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new EmployeeStatusFragment(),"Employee Status");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Employee Status");
                    }

                    else if(childPosition == 9)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new MaterialDistributionReportFragment(),"Material Distribution Report");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Material Distribution Report");

                    }

                    else if(childPosition == 10)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ReceivedPaymentReportFragment(),"Received Payment Report");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Received Payment Report");
                    }

                }

                else if(groupPosition == 1)
                {
                    if(childPosition == 0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountUpdateFragment(),"Account Update");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Account Update");

                    }

                    else if(childPosition == 1)
                    {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountAddFragment(),"Account Add");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Account");

                    }

                    else if(childPosition == 2)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountEditFragment(),"Account Edit");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Account Edit");

                    }

                }

                else if(groupPosition == 2)
                {
                    if(childPosition == 0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ItemAddFragment(),"Item Add");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Item");

                    }

                    else if(childPosition == 1)
                    {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ItemEditFragment(),"Item Edit");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Item");
                    }

                }

                else if(groupPosition == 3)
                {

                    if(childPosition == 0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new StockAddFragment(),"Stock Add");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Stock Add");

                    }

                    else if(childPosition == 1)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new StockEditFragment(),"Stock Edit");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Item");
                    }
                }

                else if(groupPosition == 4)
                {

                    if(childPosition == 0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new VendorAddFragment(),"Vendor Add");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Vendor");

                    }

                    else if(childPosition == 1)
                    {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new VendorEditFragment(),"Vendor Edit");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Vendor");
                    }
                }


                else if(groupPosition == 5)
                {

                    if(childPosition == 0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new EmployeeAttendanceFragment(),"Employee Attendance");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Employee Attendance");
                    }

                    else if(childPosition == 1)
                    {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new EmployeeAddFragment(),"Add Employee");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Employee");
                    }

                    else if(childPosition == 2)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new EmployeeEditFragment(),"Remove Employee");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Employee");

                    }
                }

                else if(groupPosition == 6)
                {

                    if(childPosition == 0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new CentreAddFragment(),"Add Store");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Employee");

                    }

                    else if(childPosition == 1)
                    {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new CentreEditFragment(),"Remove Store");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Store");
                    }
                }

                else if(groupPosition == 7)
                {

                    if(childPosition == 0)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new PurchaseEntryFragment(),"Purchase Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Purchase Entry");

                    }

                    else if(childPosition == 1)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ExpenseEntryFragment(),"Expense Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Expense Entry");
                    }

                    else if(childPosition == 2)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AttendanceEntryFragment(),"Attendance Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Attendance Entry");

                    }

                    else if(childPosition == 3)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new MaterialDistributionEntryFragment(),"Material Distribution Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Material Distribution Entry");
                    }

                    else if(childPosition == 4)
                    {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ReceivedPaymentEntryFragment(),"Received Payment Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Received Payment Entry");

                    }


                }

                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });


        //expandable list


        /*

        navigationView = (NavigationView) findViewById(R.id.navigator);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (prevItem != null)
                    prevItem.setChecked(false);

                item.setCheckable(true);
                item.setChecked(true);

                prevItem = item;


                switch (item.getItemId()) {

                    //main menu


                    case R.id.menuDashBoard:


                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new StockStatusFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(PreferencedData.getPrefDeliveryCentre(Dashboard.this));

                        drawer.closeDrawers();
                        break;



                    //nestedMenu


                    case R.id.menuCentreReport:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ChooseDateFragment(),"DateTag");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Date");


                        drawer.closeDrawers();
                        break;

                    case R.id.menuPurchaseReport:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new PurchaseReportFragment(),"Purchase Report");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Purchase Report");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuExpenseReport:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ExpenseReportFragment(),"Expense Report");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Expense Report");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuAccountAdd:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountAddFragment(),"Account Add");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Account");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuAccountBalance:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountStatusFragment(),"Account Status");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Account Status");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuAccountUpdate:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountUpdateFragment(),"Account Update");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Account Update");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuAccountEdit:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountEditFragment(),"Account Edit");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Account Edit");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuAccountLog:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AccountLogFragment(),"Account Log");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Account Log");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuStoresBalance:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new StoreStatusFragment(),"Stores Status");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Stores Status");

                        drawer.closeDrawers();
                        break;



                    case R.id.menuVendorStatus:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new VendorStatusFragment(),"Vendor Status");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Vendor Status");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuItemAdd:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ItemAddFragment(),"Item Add");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Item");

                        drawer.closeDrawers();
                        break;



                    case R.id.menuItemEdit:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ItemEditFragment(),"Item Edit");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Item");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuStockAdd:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new StockAddFragment(),"Stock Add");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Stock Add");

                        drawer.closeDrawers();
                        break;



                    case R.id.menuStockEdit:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new StockEditFragment(),"Stock Edit");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Item");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuVendorAdd:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new VendorAddFragment(),"Vendor Add");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Vendor");

                        drawer.closeDrawers();
                        break;



                    case R.id.menuVendorEdit:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new VendorEditFragment(),"Vendor Edit");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Vendor");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuEmployeeStatus:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new EmployeeStatusFragment(),"Employee Status");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Employee Status");

                        drawer.closeDrawers();
                        break;


                    case R.id.materialDistributionReport :

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new MaterialDistributionReportFragment(),"Material Distribution Report");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Material Distribution Report");

                        drawer.closeDrawers();
                        break;

                    case R.id.materialDistributionEntry :

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new MaterialDistributionEntryFragment(),"Material Distribution Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Material Distribution Entry");

                        drawer.closeDrawers();
                        break;


                    case R.id.receivedPaymentReport :

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ReceivedPaymentReportFragment(),"Received Payment Report");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Received Payment Report");

                        drawer.closeDrawers();
                        break;

                    case R.id.receivedPaymentEntry :

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ReceivedPaymentEntryFragment(),"Received Payment Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Received Payment Entry");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuEmployeeAttendance:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new EmployeeAttendanceFragment(),"Employee Attendance");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Employee Attendance");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuEmployeeAdd:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new EmployeeAddFragment(),"Add Employee");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Employee");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuEmployeeEdit:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new EmployeeEditFragment(),"Remove Employee");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Employee");

                        drawer.closeDrawers();
                        break;


                    case R.id.menuCentreAdd:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new CentreAddFragment(),"Add Store");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Add Employee");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuCentreEdit:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new CentreEditFragment(),"Remove Store");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Select Store");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuPurchaseEntry:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new PurchaseEntryFragment(),"Purchase Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Purchase Entry");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuExpenseEntry:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ExpenseEntryFragment(),"Expense Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Expense Entry");

                        drawer.closeDrawers();
                        break;

                    case R.id.menuAttendanceEntry:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new AttendanceEntryFragment(),"Attendance Entry");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Attendance Entry");

                        drawer.closeDrawers();
                        break;

                }

                return false;

            }

        });

        MenuItem item = navigationView.getMenu().findItem(R.id.menuDashBoard);
        item.setCheckable(true);
        item.setChecked(true);
        prevItem=item;

        */


        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.home_layout_id, new StockStatusFragment());
        fragmentTransaction.commit();

        toggle = new ActionBarDrawerToggle(
                this, drawer,tb,R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.closeDrawers();


        getCallPermission();
    }

    private void addItemsToExpandableList() {


        mExpandableListTitle.add("Report");
        mExpandableListTitle.add("Accounts");
        mExpandableListTitle.add("Item");
        mExpandableListTitle.add("Stock");
        mExpandableListTitle.add("Vendor");
        mExpandableListTitle.add("Employee");
        mExpandableListTitle.add("Store");
        mExpandableListTitle.add("Entries");

        // Adding child data
        List<String> Report = new ArrayList<String>();
        Report.add("Stock Status");
        Report.add("Centre");
        Report.add("Purchase");
        Report.add("Expense");
        Report.add("Accounts Log");
        Report.add("Account Status");
        Report.add("Stores Status");
        Report.add("Vendor Status");
        Report.add("Employee Status");
        Report.add("Material Distribution");
        Report.add("Received Payment");

        List<String> Accounts = new ArrayList<String>();
        Accounts.add("Amount Update");
        Accounts.add("Add");
        Accounts.add("Edit");;

        List<String> Item = new ArrayList<String>();
        Item.add("Add");
        Item.add("Edit");

        List<String> Stock = new ArrayList<String>();
        Stock.add("Add");
        Stock.add("Edit");

        List<String> Vendor = new ArrayList<String>();
        Vendor.add("Add");
        Vendor.add("Edit");

        List<String> Employee = new ArrayList<String>();
        Employee.add("Attendance and Salary");
        Employee.add("Add");
        Employee.add("Edit");


        List<String> Store = new ArrayList<String>();
        Store.add("Add");
        Store.add("Edit");


        List<String> Entries = new ArrayList<String>();
        Entries.add("Purchase");
        Entries.add("Expense");
        Entries.add("Attendance");
        Entries.add("Material Distribution");
        Entries.add("Received Payment");

        mExpandableListData.put(mExpandableListTitle.get(0), Report); // Header, Child data
        mExpandableListData.put(mExpandableListTitle.get(1), Accounts);
        mExpandableListData.put(mExpandableListTitle.get(2), Item);
        mExpandableListData.put(mExpandableListTitle.get(3), Stock);
        mExpandableListData.put(mExpandableListTitle.get(4), Vendor);
        mExpandableListData.put(mExpandableListTitle.get(5), Employee);
        mExpandableListData.put(mExpandableListTitle.get(6), Store);
        mExpandableListData.put(mExpandableListTitle.get(7), Entries);


    }

    private void getCallPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user'textViewBuyer response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        101);


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

         if(id==R.id.logOut)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setTitle("LOGOUT ?");
            builder.setMessage("Are You Sure?");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i=new Intent(Dashboard.this,LoginActivity.class);
                    PreferencedData.clearPref(Dashboard.this);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}
