package com.cdut.playtask.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cdut.playtask.data.TaskItem;

import java.util.ArrayList;

/**
 * 用于Task主界面和三个子界面之间共享数据
 */
public class TaskViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TaskItem>> dataList = new MutableLiveData<>();

    public LiveData<ArrayList<TaskItem>> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<TaskItem> newData) {
        dataList.setValue(newData);
    }
}
