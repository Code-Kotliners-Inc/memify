package com.codekotliners.memify.mocks

import com.codekotliners.memify.core.apiService.ImageEntity
import com.codekotliners.memify.core.model.ImageItem

val mockImagesApi =
    listOf<ImageEntity>(
        ImageEntity(
            id = 1,
            title = "Lol",
            url = "https://yandex-images.clstorage.net/XH10e0368/f1abffk2f/0yRD9FbD7EJ_j_lx-nFJENRb_IVSb9Mz0ie2KoEIiK67hGUSc4ND-1DPxI6Z8kDgKAktiHhQKtFLi4puACZF9p4xFukTxpVef5uoXdNBISCUDszwXjrbV8I60hqR-TgnZk3Jtr5D2gP8roH-jet4nQUNwDF9oN-k6u8nNhiLdEPHcAuQ3puJQlMnrF2qEf285L7sDf5ix1ZqD59zmVUQH4ZhKVZKA4L_3P5RXk2XLY2tbYz0uVjvYHZbr7Jch8wnz7TboGanASLvU9yFJt0xQPQKmJVqJt-aMgtjv22xHA9mBVUC0lvqTwwOySYtd0GtQTHsYOkIH5wq05ua4Kt8cxdIG6g6DzGyF9PoCaLpURicMqwZIl43Xs6PC3fhuQyzsmUtHsbzpmfgzpHqud8lgYHJ9ABB3KPwqvPnstR3QJtr-GN0nt-VnkcXAHlaoW1cxI5cXRaOa1aeNztvyVVI5_4F1UIm81qTJF6h_l2bEWF91SSQJTwjDBp_PzpI_yhrG9DDsHob-bL7Q5QhyiWpIPT6lNXmEq-2ilcfyzVNTEvG4Y22Site61SuCQK18wk5rV1oFHWsC3QSYy-CaKdkp5s4Yxj-wzFar4uE2bLlZQjcomjVknrLomYrx2flWewX1mUV1v5TXoN88o2aTatZxa0dYCipICeozsMTCuALaIcPIF_kgsvFdoNT3PkGpRU09FJUYRb6Q6KeYw8bYU0cw0aNFQYeR_53QNaZkvmnBRkJcbBIXUwfnCb_L3KYCzRD6xzD-N7rLdKXNzABhvFBMKgWkBleGvMSTitDj2Gl0Gu2pQEiHutmf2T2iW79140xmWWIYNFY8zQqB68-VEugb8_wf5hmqxGamy888cIx9ZCksoQBVtZzyv4Pj-OFbdRHTt2NTmIrHseAUsHixUsJtfWRtBB5HINs-g_DsuifFMMHsBMYWuv5yo_f0MHWoU2QZCrYfXqK976a1-uPUWWM",
            width = 736,
            height = 1104,
        ),
        ImageEntity(
            id = 2,
            title = "Lol",
            url = "https://i3.wp.com/avatars.dzeninfra.ru/get-zen_doc/3725294/pub_5f32d2ad148048284afc3e98_5f32d8ac5660ed705924987e/scale_1200?ssl=1",
            width = 1200,
            height = 800,
        ),
        ImageEntity(
            id = 3,
            title = "Lol",
            url = "https://avatars.mds.yandex.net/i?id=8d614fc6546d653580d7eeebed28f973_l-7054193-images-thumbs&n=13",
            width = 1280,
            height = 720,
        ),
        ImageEntity(
            id = 4,
            title = "Lol",
            url = "https://cdn1.ozone.ru/s3/multimedia-m/6460779826.jpg",
            width = 1280,
            height = 720,
        ),
    )

var mockImagesRepository =
    mockImagesApi.map { it ->
        ImageItem(id = it.id, title = it.title, url = it.url, localPath = null, width = it.width, height = it.height)
    }
