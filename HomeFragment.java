package com.saahayak.saahayak.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.saahayak.saahayak.MainActivity;
import com.saahayak.saahayak.R;
import com.saahayak.saahayak.Utils.App;
import com.saahayak.saahayak.Utils.Constants;
import com.saahayak.saahayak.Utils.MyMVVM;
import com.saahayak.saahayak.VedioPlayerActivity;
import com.saahayak.saahayak.adapters.AdapterAll;
import com.saahayak.saahayak.adapters.AdapterOffers;
import com.saahayak.saahayak.adapters.AdapterPopular;
import com.saahayak.saahayak.adapters.AdapterReviews;
import com.saahayak.saahayak.adapters.HomeViewPagerAdapter2;
import com.saahayak.saahayak.response.AllServicePojo;
import com.saahayak.saahayak.response.BannerVedioPojo;
import com.saahayak.saahayak.response.HotDealPojo;
import com.saahayak.saahayak.response.PopularServicePojo;
import com.saahayak.saahayak.response.RegisterPojo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;

import static com.saahayak.saahayak.R.drawable.ic_pause;


public class HomeFragment extends Fragment implements View.OnClickListener {


    private View view, viewSideMenu, customDialog;
    private ViewPager2 viewPager;
    private CircleIndicator3 circleIndicator;
    private MyMVVM myMVVM;
    private ImageView search;
    private CircleImageView user_image;
    private RecyclerView popularRV, allRV, reviewsRV, offerRV;
    private TextView popularTxt, allTxt;
    private HomeViewPagerAdapter2 adapterHomeViewpager;
    private AdapterAll adapterAll;
    private AdapterPopular adapterPopular;
    private AdapterOffers adapterOffers;
    private AdapterReviews adapterReviews;
    List<AllServicePojo.Detail> allServices = new ArrayList<>();
    List<PopularServicePojo.Detail> popallServices = new ArrayList<>();
    List<HotDealPojo.Detail> hotDeals = new ArrayList<>();
    List<BannerVedioPojo.$detail> bannervedioList = new ArrayList<>();
    Bundle salon;
    Dialog dialog;
    NavigationView navView;
    DrawerLayout navMain;
    private String Image_user, id;


    ArrayList<Integer> imgList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            myMVVM = ViewModelProviders.of(HomeFragment.this).get(MyMVVM.class);
            id = App.getSharedpref().getModel(Constants.User_Register, RegisterPojo.class).getDetails().getId();
            Image_user = App.getSharedpref().getModel(Constants.User_Register, RegisterPojo.class).getDetails().getImage();
            salon = new Bundle();
            dialog = new Dialog(getActivity());
            dialog.setCancelable(true);
            customDialog = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);

            dialog.setContentView(customDialog);


            findIDs();
            setAdapterAllService();
            setAdapterPopService();
            setBannerVideo();
            setHotDealAdapter();
            popularRV.setAdapter(adapterPopular);
            allRV.setAdapter(adapterAll);
            adapterReviews = new AdapterReviews();
            reviewsRV.setAdapter(adapterReviews);

        }
        return view;
    }

    // private void detailApi() {
//     myMVVM.AllUserDetail(getActivity(), id).observe(getViewLifecycleOwner(), new Observer<RegisterPojo>() {
//         @Override
//         public void onChanged(RegisterPojo userDetailPojo) {
//             if (userDetailPojo.getSuccess().equalsIgnoreCase("1")) {
//                 image = userDetailPojo.getDetails().getImage();
//             } else {
//                 Toast.makeText(getActivity(), userDetailPojo.getMessage(), Toast.LENGTH_SHORT).show();
//             }
//         }
//     });
// }
    private void setHotDealAdapter() {
        myMVVM.AllHotDeal(getActivity()).observe(getViewLifecycleOwner(), new Observer<HotDealPojo>() {
            @Override
            public void onChanged(HotDealPojo hotDealPojo) {
                if (hotDealPojo.getSuccess().equalsIgnoreCase("1")) {
                    hotDeals = hotDealPojo.getDetails();
                    adapterOffers = new AdapterOffers(getActivity(), hotDeals, new AdapterOffers.Select() {
                        @Override
                        public void onClick(int position) {
                            Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();
                        }
                    });
                    SnapHelper snapHelper = new LinearSnapHelper();
                    snapHelper.attachToRecyclerView(offerRV);
                    offerRV.setAdapter(adapterOffers);

                }
            }
        });
    }

    private void setBannerVideo() {
        myMVVM.AllVedioList(getActivity()).observe(getViewLifecycleOwner(), new Observer<BannerVedioPojo>() {
            @Override
            public void onChanged(BannerVedioPojo bannerVedioPojo) {
                if (bannerVedioPojo.getSuccess().equalsIgnoreCase("1")) {
                    bannervedioList = bannerVedioPojo.get$details();
                    adapterHomeViewpager = new HomeViewPagerAdapter2(getActivity(), bannervedioList, new HomeViewPagerAdapter2.Select() {
                        @Override
                        public void onClick(int position, String path) {
                            Intent intent = new Intent(getActivity(), VedioPlayerActivity.class);
                            intent.putExtra("vedio", path);
                            startActivity(intent);
                        }
                    });
                    viewPager.setAdapter(adapterHomeViewpager);
                    circleIndicator.setViewPager(viewPager);

                    viewPager.setClipToPadding(false);
                    viewPager.setClipChildren(false);
                    viewPager.setOffscreenPageLimit(2);
                    viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(60));
                    compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View page, float position) {
                            float r = 1 - Math.abs(position);
                            page.setScaleY(0.85f + r * 0.15f);
                            page.setScaleY(0.85f + r * 0.15f);
                        }
                    });

                    viewPager.setPageTransformer(compositePageTransformer);
                }
            }
        });
    }

    private void setAdapterPopService() {
        myMVVM.PopularAllList(getActivity()).observe(getViewLifecycleOwner(), new Observer<PopularServicePojo>() {
            @Override
            public void onChanged(PopularServicePojo popularServicePojo) {
                if (popularServicePojo.getSuccess().equalsIgnoreCase("1")) {
                    popallServices = popularServicePojo.getDetails();
                    adapterPopular = new AdapterPopular(getActivity(), popallServices, new AdapterPopular.SelectPopular() {
                        @Override
                        public void onClick(int position, String name, String description, String scope, String popImagePath, String service_id) {
                            salon.putString("name", name);
                            salon.putString("description", description);
                            salon.putString("scope", scope);
                            salon.putString("image", popImagePath);
                            salon.putString("service_id", service_id);
                            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_serviceDetailFragment, salon);
                        }
                    });
                    popularRV.setAdapter(adapterPopular);
                }
            }
        });
    }

    private void setAdapterAllService() {
        myMVVM.JugaarListAll(getActivity()).observe(getViewLifecycleOwner(), new Observer<AllServicePojo>() {
            @Override
            public void onChanged(AllServicePojo allServicePojo) {
                if (allServicePojo.getSuccess().equalsIgnoreCase("1")) {
                    allServices = allServicePojo.getDetails();
                    adapterAll = new AdapterAll(getActivity(), allServices, new AdapterAll.SelectAllService() {
                        @Override
                        public void onclick(int position, String name, String description, String scope, String imagePath, String service_id) {

//                            Bundle  salon=new Bundle();
                            salon.putString("name", name);
                            salon.putString("description", description);
                            salon.putString("scope", scope);
                            salon.putString("image", imagePath);
                            salon.putString("service_id", service_id);

                            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_serviceDetailFragment, salon);
                        }
                    });
                    allRV.setAdapter(adapterAll);
                }
            }
        });

    }

    private void findIDs() {


        viewPager = view.findViewById(R.id.viewPager);
        search = view.findViewById(R.id.search);
        circleIndicator = view.findViewById(R.id.circleIndicator);


        search.setOnClickListener(this);

        popularRV = view.findViewById(R.id.popularRV);
        allRV = view.findViewById(R.id.allRV);
        reviewsRV = view.findViewById(R.id.reviewsRV);
        offerRV = view.findViewById(R.id.offersRV);

        popularTxt = view.findViewById(R.id.popularTxt);
        allTxt = view.findViewById(R.id.allTxt);

        view.findViewById(R.id.popularTxt).setOnClickListener(this);
        view.findViewById(R.id.allTxt).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.cartBtn).setOnClickListener(this);
        view.findViewById(R.id.referImg).setOnClickListener(this);

//        Dialog button actions
        customDialog.findViewById(R.id.bookBtn).setOnClickListener(this);
        customDialog.findViewById(R.id.laterBtn).setOnClickListener(this);

//        Side menu Ids
        navView = getActivity().findViewById(R.id.navView);
        navMain = getActivity().findViewById(R.id.navMain);

        viewSideMenu = navView.getHeaderView(0);

        viewSideMenu.findViewById(R.id.profile).setOnClickListener(this);
        viewSideMenu.findViewById(R.id.bookings).setOnClickListener(this);
        viewSideMenu.findViewById(R.id.myOrders).setOnClickListener(this);
        viewSideMenu.findViewById(R.id.myWallet).setOnClickListener(this);
        viewSideMenu.findViewById(R.id.offers).setOnClickListener(this);
        viewSideMenu.findViewById(R.id.chat).setOnClickListener(this);
        viewSideMenu.findViewById(R.id.terms).setOnClickListener(this);
        viewSideMenu.findViewById(R.id.contact).setOnClickListener(this);
        viewSideMenu.findViewById(R.id.logout).setOnClickListener(this);


        user_image = viewSideMenu.findViewById(R.id.user_image);

        if (Image_user.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "hii", Toast.LENGTH_SHORT).show();
            user_image.setImageResource(R.drawable.ic_user);
        } else {
            Glide.with(getActivity()).load(Image_user).into(user_image);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popularTxt:
                allRV.setVisibility(View.GONE);
                allTxt.setBackground(null);
                allTxt.setTextColor(getResources().getColor(R.color.black));
                popularRV.setVisibility(View.VISIBLE);
                popularTxt.setBackground(getResources().getDrawable(R.drawable.popular_bg));
                popularTxt.setTextColor(getResources().getColor(R.color.white));
                break;

            case R.id.allTxt:
                popularRV.setVisibility(View.GONE);
                popularTxt.setBackground(null);
                popularTxt.setTextColor(getResources().getColor(R.color.black));
                allRV.setVisibility(View.VISIBLE);
                allTxt.setBackground(getResources().getDrawable(R.drawable.all_bg));
                allTxt.setTextColor(getResources().getColor(R.color.white));

                break;
            case R.id.cartBtn:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_cartFragment);
                break;
            case R.id.referImg:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_referralFragment);
                break;
            case R.id.menu:
                navMain.open();

                break;

//                Side menu click listners
            case R.id.profile:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_profileFragment);
                break;
            case R.id.bookings:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_bookingsFragment);
                break;
            case R.id.myOrders:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_myOrdersFragment);
                break;
            case R.id.myWallet:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_myWalletFragment);
                break;
            case R.id.offers:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_offersFragment);
                break;
            case R.id.chat:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_chatFragment);
                break;
            case R.id.terms:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_termsFragment);
                break;
            case R.id.contact:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_contactUsFragment);
                break;
            case R.id.logout:
                navMain.close();
                navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                new AlertDialog.Builder(getActivity())
                        .setTitle("LOGOUT")
                        .setMessage("Are you sure you want to LOGOUT?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                App.getSharedpref().clearPreferences();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_warning)
                        .show();


                break;
            case R.id.search:
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_homeSeachFragment);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        navMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Image_user = App.getSharedpref().getModel(Constants.User_Register, RegisterPojo.class).getDetails().getImage();
        if (Image_user.equalsIgnoreCase("")) {
            user_image.setImageResource(R.drawable.ic_user);
        } else {
            Glide.with(getActivity()).load(Image_user).into(user_image);
        }
    }
}