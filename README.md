# KeyboardAnimator
- 키보드 노출에 의한 레이아웃 애니메이션 적용
- 키보드 노출시 레이아웃이 텔레포트 하는 현상을 키보드 노출 애니메이터와 동일하게 맞춰서 자연스럽게 하기 위함
- [MainActivity#27](https://github.com/ikmuwn/Mock-android/blob/599b2181761df2ac88a91ecfc78162c73d5bf1d2/app/src/main/java/kim/uno/mock/ui/main/MainActivity.kt#L27)
- [Mock-android](https://github.com/ikmuwn/Mock-android)

## Use

- 변경될 layout container level 에서 지정

  ```kotlin
    override fun setContentView(view: View?) {
        super.setContentView(view)
        view?.applyKeyboardInsetsAnimator()
    }
  ```
  
- BottomSheetDialog에서 사용하고자 하는 경우에는 하위버전에서는 decorView를 activity와 경우하는 것이 확인되니 R 이상에서만 동작하도록 분기해야함

  ```kotlin
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window?.decorView?.applyKeyboardInsetsAnimator()
  }
  ```
