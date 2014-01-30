package net.wespot.pim;

import android.annotation.TargetApi;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.*;
import net.wespot.pim.controller.Adapters.AppSectionsPagerAdapter;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.utils.layout.DrawerActionBarBaseFragActivity;
import daoBase.DaoConfiguration;
import org.celstec.arlearn.delegators.INQ;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends DrawerActionBarBaseFragActivity implements ActionBar.TabListener {
    private ViewPager mViewPager;
    private ListView mListInquiries;
    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private InquiryLazyListAdapter adapterInq;

    /**
     * Called when the activity is first created.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                getmActionBarHelper().setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            getmActionBarHelper().addTab(
                    getmActionBarHelper().newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        // Set up de ListView of menu inquiries
        // TODO can be setted up in parent to be accesible for all drawer layout
        INQ.init(this);
        INQ.inquiry.syncInquiries();
        INQ.inquiry.syncHypothesis(151l);

        DaoConfiguration daoConfiguration= DaoConfiguration.getInstance(this);
        adapterInq =  new InquiryLazyListAdapter(this);

        mListInquiries = (ListView) findViewById(R.id.inquiries_list_menu);
        mListInquiries.setAdapter(adapterInq);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                if(getmDrawerLayout().isDrawerOpen(GravityCompat.START)){
                    getmDrawerLayout().closeDrawer(Gravity.START);
                }else{
                    getmDrawerLayout().openDrawer(GravityCompat.START);
                }
                break;
            case R.id.menu_search:
                Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
                onSearchRequested();
                break;
            case R.id.menu_share:
                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
                break;
            // add case implemented on inquiresFragment.java

        }
        return super.onOptionsItemSelected(item);
    }



}
