[![](https://jitpack.io/v/caoyangim/PhotoSelector.svg)](https://jitpack.io/#caoyangim/PhotoSelector)

# PhotoSelector

a simple photo selector demo

``` gradle
    implementation 'com.github.caoyangim:PhotoSelector:Tag'
```

## use

``` kotlin
  // 配置（必选）
  PhotoSelectorConfig.setImageLoader { imageView, uri ->
       // Glide Coil ...
       imageView.load(uri)
  }
  // 使用
  PhotoSelector.with(this)
           .useSystemAlbum(system)
           .setMaxSelectItem(maxItem)
           .takePhoto(binding.switchTakePhoto.isChecked)
           .take { uriList ->
               // todo
           }
```

## TODO

1. 选中回显
2. 图片预览
