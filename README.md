[![](https://jitpack.io/v/caoyangim/PhotoSelector.svg)](https://jitpack.io/#caoyangim/PhotoSelector)
# PhotoSelector
a simple photo selector demo

``` gradle
dependencies {
       implementation 'com.github.caoyangim:PhotoSelector:Tag'
}
```

## use
``` kotlin
   private val resultHelper = ActivityResultHelper(requireActivity(), PhotoSelectActivity.contract)
   private fun choosePhotoSys(system: Boolean = true) {
        if (system) {
            PickResultFragment.launch(
                supportFragmentManager,
                maxSize
            ) { uris ->
                // showUris
            }
            return
        }
        PhotoSelectActivity.launch(resultHelper, PhotoRequest(maxSize)) { uris ->
            // showUris
        }
    }
```
