package dev.snippets.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.snippets.BuildConfig
import dev.snippets.data.network.Api
import dev.snippets.data.Repository
import dev.snippets.data.local.SharedPrefHelper
import dev.snippets.data.local.SnippetsDao
import dev.snippets.data.local.SnippetsDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpLoggingInterceptor() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(sharedPref: SharedPrefHelper): Retrofit {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(provideOkHttpLoggingInterceptor())
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer ${sharedPref.accessToken}")
                        .build()
                chain.proceed(request)
            }.build())
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    fun provideRepository(api: Api, dao: SnippetsDao) = Repository(api, dao)

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context) = SharedPrefHelper(context)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        SnippetsDatabase::class.java,
        "snippets-db"
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: SnippetsDatabase) = database.snippetsDao()
}