package com.softfinite.RoomDb;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

public class TruckViewModel extends AndroidViewModel {

    private TruckRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Truck>> mAllWordsLive;
    private LiveData<List<Truck>> mgetFromTruckAndDate;

    String truckNumber, date;

    public TruckViewModel(Application application) {
        super(application);
        mRepository = new TruckRepository(application, truckNumber, date);
        mAllWordsLive = mRepository.getAllTruckDataLive();
        mgetFromTruckAndDate = mRepository.getFromTruckAndDate();
    }

    public LiveData<List<Truck>> getAllWordsLive() {
        return mAllWordsLive;
    }

    public LiveData<List<Truck>> getFromTruckAndDate(String truckNumber, String date) {
        mRepository = new TruckRepository(getApplication(), truckNumber, date);
        mgetFromTruckAndDate = mRepository.getFromTruckAndDate();
        return mgetFromTruckAndDate;
    }

    public void insert(Truck truck) {
        mRepository.insert(truck);
    }
}
