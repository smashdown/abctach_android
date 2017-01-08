package com.abctech.blogtalking.module.imagepicker.event;


import com.abctech.blogtalking.module.imagepicker.model.HSImageFolderItem;

public class HSEventImageFolderSelected {
    public HSImageFolderItem item;

    public HSEventImageFolderSelected(HSImageFolderItem folderItem) {
        this.item = folderItem;
    }
}
