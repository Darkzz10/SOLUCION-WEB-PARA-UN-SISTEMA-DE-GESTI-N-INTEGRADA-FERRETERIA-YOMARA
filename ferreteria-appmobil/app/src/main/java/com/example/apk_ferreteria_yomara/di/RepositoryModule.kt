package com.example.apk_ferreteria_yomara.di

import com.example.apk_ferreteria_yomara.data.repository.AuthRepositoryImpl
import com.example.apk_ferreteria_yomara.data.repository.ProductRepositoryImpl
import com.example.apk_ferreteria_yomara.domain.repository.IAuthRepository
import com.example.apk_ferreteria_yomara.domain.repository.IProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): IAuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): IProductRepository
}