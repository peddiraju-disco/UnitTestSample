package com.example.unittestsample

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {

    companion object {
        private const val TEST_NAME = "Test name"
        private const val TEST_EMAIL = "test@email.com"
        private val TEST_DATE_OF_BIRTH = Calendar.getInstance()

        init {
            TEST_DATE_OF_BIRTH[1980, 1] = 1
        }
    }

    private var mSharedPreferenceEntry: SharedPreferenceEntry? = null
    private var mMockSharedPreferencesHelper: SharedPreferencesHelper? = null
    private var mMockBrokenSharedPreferencesHelper: SharedPreferencesHelper? = null

    @Mock
    var mMockSharedPreferences: SharedPreferences? = null

    @Mock
    var mMockBrokenSharedPreferences: SharedPreferences? = null

    @Mock
    var mMockEditor: Editor? = null

    @Mock
    var mMockBrokenEditor: Editor? = null

    @Before
    fun initMocks() {

        mSharedPreferenceEntry = SharedPreferenceEntry(
            TEST_NAME, TEST_DATE_OF_BIRTH,
            TEST_EMAIL
        )

        mMockSharedPreferencesHelper = createMockSharedPreference()

        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference()
    }

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {

        val success = mMockSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry!!)
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.save... returns true",
            success, CoreMatchers.`is`(true)
        )

        val savedSharedPreferenceEntry = mMockSharedPreferencesHelper!!.personalInfo

        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.name has been persisted and read correctly",
            mSharedPreferenceEntry!!.name,
            CoreMatchers.`is`(CoreMatchers.equalTo(savedSharedPreferenceEntry.name))
        )
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry!!.dateOfBirth,
            CoreMatchers.`is`(CoreMatchers.equalTo(savedSharedPreferenceEntry.dateOfBirth))
        )
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.email has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry!!.email,
            CoreMatchers.`is`(CoreMatchers.equalTo(savedSharedPreferenceEntry.email))
        )
    }

    @Test
    fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {

        val success =
            mMockBrokenSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry!!)
        MatcherAssert.assertThat(
            "Makes sure writing to a broken SharedPreferencesHelper returns false", success,
            CoreMatchers.`is`(false)
        )
    }


    private fun createMockSharedPreference(): SharedPreferencesHelper {

        Mockito.`when`(
            mMockSharedPreferences!!.getString(
                ArgumentMatchers.eq(
                    SharedPreferencesHelper.KEY_NAME
                ), ArgumentMatchers.anyString()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.name)
        Mockito.`when`(
            mMockSharedPreferences!!.getString(
                ArgumentMatchers.eq(
                    SharedPreferencesHelper.KEY_EMAIL
                ), ArgumentMatchers.anyString()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.email)
        Mockito.`when`(
            mMockSharedPreferences!!.getLong(
                ArgumentMatchers.eq(SharedPreferencesHelper.KEY_DOB),
                ArgumentMatchers.anyLong()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.dateOfBirth.timeInMillis)

        Mockito.`when`(mMockEditor!!.commit()).thenReturn(true)

        Mockito.`when`(mMockSharedPreferences!!.edit()).thenReturn(mMockEditor)
        return SharedPreferencesHelper(mMockSharedPreferences!!)
    }


    private fun createBrokenMockSharedPreference(): SharedPreferencesHelper {

        Mockito.`when`(mMockBrokenEditor!!.commit()).thenReturn(false)

        Mockito.`when`(mMockBrokenSharedPreferences!!.edit()).thenReturn(mMockBrokenEditor)

        return SharedPreferencesHelper(mMockBrokenSharedPreferences!!)
    }
}