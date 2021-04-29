package dev.pimentel.counters.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dev.pimentel.counters.data.R
import dev.pimentel.counters.data.database.CountersDatabase
import dev.pimentel.counters.data.generator.IdGenerator
import dev.pimentel.counters.data.generator.IdGeneratorImpl
import dev.pimentel.counters.data.repository.CountersRepositoryImpl
import dev.pimentel.counters.data.repository.PreferencesRepositoryImpl
import dev.pimentel.counters.data.sources.local.CountersLocalDataSource
import dev.pimentel.counters.data.sources.local.PreferencesLocalDataSource
import dev.pimentel.counters.data.sources.local.PreferencesLocalDataSourceImpl
import dev.pimentel.counters.data.sources.remote.CountersRemoteDataSource
import dev.pimentel.counters.domain.repository.CountersRepository
import dev.pimentel.counters.domain.repository.PreferencesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModules {

    private const val REQUEST_TIMEOUT = 60L

    // region REMOTE
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        @ApplicationContext context: Context,
        moshi: Moshi
    ): Retrofit {
        val apiUrl = context.getString(R.string.api_url)

        val client = OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideCountersRemoteDataSource(retrofit: Retrofit): CountersRemoteDataSource =
        retrofit.create(CountersRemoteDataSource::class.java)
    // endregion

    // region LOCAL
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            CountersDatabase::class.java,
            CountersDatabase::class.simpleName!!
        ).build()

    @Provides
    @Singleton
    fun provideIdGenerator(): IdGenerator = IdGeneratorImpl()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferencesLocalDataSource(sharedPreferences: SharedPreferences): PreferencesLocalDataSource =
        PreferencesLocalDataSourceImpl(sharedPreferences = sharedPreferences)

    @Provides
    @Singleton
    fun provideCountersLocalDataSource(countersDatabase: CountersDatabase): CountersLocalDataSource =
        countersDatabase.createCountersLocalDataSource()
    // endregion

    // region REPOSITORY
    @Provides
    @Singleton
    fun provideCountersRepository(
        countersRemoteDataSource: CountersRemoteDataSource,
        countersLocalDataSource: CountersLocalDataSource,
        idGenerator: IdGenerator,
    ): CountersRepository = CountersRepositoryImpl(
        countersRemoteDataSource = countersRemoteDataSource,
        countersLocalDataSource = countersLocalDataSource,
        idGenerator = idGenerator
    )

    @Provides
    @Singleton
    fun providePreferencesRepository(preferencesLocalDataSource: PreferencesLocalDataSource): PreferencesRepository =
        PreferencesRepositoryImpl(preferencesLocalDataSource = preferencesLocalDataSource)
    // endregion
}
