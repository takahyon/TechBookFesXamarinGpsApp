package com.amay077.gpsapp.views.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amay077.gpsapp.App;
import com.amay077.gpsapp.R;
import com.amay077.gpsapp.databinding.ActivityMainBinding;
import com.amay077.gpsapp.di.ActivityModule;
import com.amay077.gpsapp.messengers.ShowToastMessages;
import com.amay077.gpsapp.messengers.StartActivityMessage;
import com.amay077.gpsapp.viewmodel.MainViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    @Inject
    /*private final*/ MainViewModel _viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Inject by Dagger2
        final App app = (App) getApplication();
        app.getApplicationComponent().inject(this);

        binding.setViewModel(_viewModel);

        // ■ViewModel からの Message の受信

        // 画面遷移のメッセージ受信
        _viewModel.messenger.register(StartActivityMessage.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(m -> {
                        Intent intent = new Intent(MainActivity.this, m.activityClass);
                        MainActivity.this.startActivity(intent);
                });

        // トースト表示のメッセージ受信
        _viewModel.messenger.register(ShowToastMessages.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(m -> {
                    Toast.makeText(MainActivity.this, m.text, Toast.LENGTH_LONG).show();
                });
    }
}
