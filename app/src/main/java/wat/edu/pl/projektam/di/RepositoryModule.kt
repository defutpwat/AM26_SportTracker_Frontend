package wat.edu.pl.projektam.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import wat.edu.pl.projektam.data.repository.AuthRepositoryImpl
import wat.edu.pl.projektam.data.repository.WorkoutRepositoryImpl
import wat.edu.pl.projektam.domain.repository.AuthRepository
import wat.edu.pl.projektam.domain.repository.WorkoutRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository
}
