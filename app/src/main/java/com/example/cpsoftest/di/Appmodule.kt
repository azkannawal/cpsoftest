package com.example.cpsoftest.di

import android.content.Context
import androidx.room.Room
import com.example.cpsoftest.data.local.AppDatabase
import com.example.cpsoftest.data.local.dao.CityDao
import com.example.cpsoftest.data.local.dao.UserDao
import com.example.cpsoftest.data.remote.CityService
import com.example.cpsoftest.data.remote.UserService
import com.example.cpsoftest.data.repository.CityRepositoryImpl
import com.example.cpsoftest.data.repository.UserRepositoryImpl
import com.example.cpsoftest.domain.repository.CityRepository
import com.example.cpsoftest.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindCityRepository(impl: CityRepositoryImpl): CityRepository

    companion object {

        private const val BASE_URL = "https://661f555f16358961cd940b83.mockapi.io/api/v2/accurate/"

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideUserService(retrofit: Retrofit): UserService =
            retrofit.create(UserService::class.java)

        @Provides
        @Singleton
        fun provideCityService(retrofit: Retrofit): CityService =
            retrofit.create(CityService::class.java)

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "cpsoftest.db"
            ).build()

        @Provides
        fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

        @Provides
        fun provideCityDao(db: AppDatabase): CityDao = db.cityDao()
    }
}