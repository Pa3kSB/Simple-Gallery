import android.app.Instrumentation
import android.content.Context
import androidx.test.InstrumentationRegistry
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.adapters.MyRecyclerViewAdapter
import com.simplemobiletools.commons.extensions.formatSize
import com.simplemobiletools.commons.helpers.*
import com.simplemobiletools.commons.views.MyRecyclerView
import com.simplemobiletools.gallery.activities.SimpleActivity
import com.simplemobiletools.gallery.adapters.DirectoryAdapter
import com.simplemobiletools.gallery.extensions.config
import com.simplemobiletools.gallery.extensions.isThisOrParentExcluded
import com.simplemobiletools.gallery.extensions.isThisOrParentIncluded
import com.simplemobiletools.gallery.helpers.*
import com.simplemobiletools.gallery.models.Directory
import com.simplemobiletools.gallery.models.Medium
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SortingTests {

//    @Test
//    fun dajakyTest() {
//        val baseActivity = SimpleActivity()
//
//        val newFolder = Directory(null, "", "", "", 0, 0, 0, 0L, 0, 0)
//        val dirs = ArrayList<Directory>()
//        for (i in 0..10) dirs.add(newFolder)
//
//        val directoryAdapter = DirectoryAdapter(baseActivity, dirs, null, MyRecyclerView(InstrumentationRegistry.getContext()),
//        false, null, {})
//        assertEquals(dirs.size, directoryAdapter.itemCount, "itemsCount should be equal to ${dirs.size}")
//    }

    @Test
    fun isThisOrParentIncludedTests() {
        val paths = hashSetOf("dir000", "dir001", "dir002")
        assertTrue("dir001".isThisOrParentIncluded(paths), "dir001 should be included")

//        val paths = hashSetOf("dir000", "dir002", "dir002")
//        assertFalse("dir001".isThisOrParentIncluded(paths), "dir001 should be included")
//        val paths = hashSetOf("dir000", "dir001", "dir002")
//        assertTrue("dir001".isThisOrParentIncluded(paths), "dir001 should be included")
    }

    @Test
    fun isThisOrParentExcludedTests() {
        val paths = hashSetOf("dir000", "dir001", "dir002")
        assertTrue("dir001".isThisOrParentExcluded(paths), "dir001 should be excluded")

//        val paths = hashSetOf("dir000", "dir002", "dir002")
//        assertFalse("dir001".isThisOrParentIncluded(paths), "dir001 should be included")
//        val paths = hashSetOf("dir000", "dir001", "dir002")
//        assertTrue("dir001".isThisOrParentIncluded(paths), "dir001 should be included")
    }

    @Test
    fun sortMediaByNameTests() {
        val smallest = Medium(0, "ahoj", "", "", 0, 0, 0, 0, false, 0)
        val biggest = Medium(1, "fahoj", "", "", 0, 0, 0, 0, false, 0)

        val mediums = arrayListOf<Medium>(
                Medium(2, "bahoj", "", "", 0, 0, 0, 0, false, 0),
                smallest,
                Medium(3, "cahoj", "", "", 0, 0, 0, 0, false, 0),
                biggest,
                Medium(4, "dahoj", "", "", 0, 0, 0, 0, false, 0)
        )

        MediaFetcher.Companion.sortMedia(mediums, SORT_BY_NAME)
        assertEquals(smallest, mediums.first(), "First should be `ahoj`")
        assertEquals(biggest, mediums.last(), "Last should be `fahoj`")
    }

    

    @Test
    fun sortMediaByDateTakenTests() {
        val earliest = Medium(0, "", "", "", 0, 0, 0, 0, false, 0)
        val latest = Medium(1, "", "", "", 0, 10, 0, 0, false, 0)

        val mediums = arrayListOf<Medium>(
                Medium(2, "", "", "", 0, 4, 0, 0, false, 0),
                earliest,
                Medium(3, "", "", "", 0, 8, 0, 0, false, 0),
                latest,
                Medium(4, "", "", "", 0, 7, 0, 0, false, 0)
        )

        MediaFetcher.Companion.sortMedia(mediums, SORT_BY_DATE_TAKEN)
        assertEquals(earliest, mediums.first(), "First should be `0`")
        assertEquals(latest, mediums.last(), "Last should be `10`")
    }

    @Test
    fun sortMediaByModifiedTests() {
        val first = Medium(0, "", "", "", 2, 0, 0, 0, false, 0)
        val last = Medium(1, "", "", "", 15, 0, 0, 0, false, 0)

        val mediums = arrayListOf<Medium>(
                Medium(2, "", "", "", 7, 0, 0, 0, false, 0),
                first,
                Medium(3, "", "", "", 9, 0, 0, 0, false, 0),
                last,
                Medium(4, "", "", "", 4, 0, 0, 0, false, 0)
        )

        MediaFetcher.Companion.sortMedia(mediums, SORT_BY_DATE_MODIFIED)
        assertEquals(first, mediums.first(), "First should be `2`")
        assertEquals(last, mediums.last(), "Last should be `15`")
    }

    @Test
    fun sortMediaBySizeTests() {
        val smallest = Medium(0, "", "", "", 0, 0, 1, 0, false, 0)
        val biggest = Medium(1, "", "", "", 0, 0, 8, 0, false, 0)

        val mediums = arrayListOf<Medium>(
                Medium(2, "", "", "", 0, 0, 7, 0, false, 0),
                smallest,
                Medium(3, "", "", "", 0, 0, 5, 0, false, 0),
                biggest,
                Medium(4, "", "", "", 0, 0, 3, 0, false, 0)
        )

        MediaFetcher.Companion.sortMedia(mediums, SORT_BY_SIZE)
        assertEquals(smallest, mediums.first(), "Smallest should be `1`")
        assertEquals(biggest, mediums.last(), "Biggest should be `8`")
    }

    @Test
    fun sortMediaByPathTests() {
        val smallest = Medium(0, "", "/album", "", 0, 0, 0, 0, false, 0)
        val biggest = Medium(1, "", "/pictures", "", 0, 0, 0, 0, false, 0)

        val mediums = arrayListOf<Medium>(
                Medium(2, "", "/download", "", 0, 0, 0, 0, false, 0),
                smallest,
                Medium(3, "", "/desktop", "", 0, 0, 0, 0, false, 0),
                biggest,
                Medium(4, "", "/documents", "", 0, 0, 0, 0, false, 0)
        )

        MediaFetcher.Companion.sortMedia(mediums, SORT_BY_PATH)
        assertEquals(smallest, mediums.first(), "First should be `/album`")
        assertEquals(biggest, mediums.last(), "Last should be `pictures`")
    }

    @Test
    fun isGifTest() {
        val gifMedia = Medium(0, "", "", "", 0, 0, 0, TYPE_GIFS, false, 0)
        val nonGifMedia = Medium(0, "", "", "", 0, 0, 0, TYPE_RAWS, false, 0)

        assertTrue(gifMedia.isGIF(), "Photo is gif")
        assertFalse(nonGifMedia.isGIF(), "Photo is not gif")
    }

    @Test
    fun isHiddenTest() {
        val isHidden = Medium(0, ".name", "", "", 0, 0, 0, TYPE_GIFS, false, 0)
        val nonHidden = Medium(0, "name", "", "", 0, 0, 0, TYPE_RAWS, false, 0)

        assertTrue(isHidden.isHidden(), "Photo is hidden")
        assertFalse(nonHidden.isHidden(), "Photo is not hidden")
    }


    @Test
    fun isBubbleTest() {
        val medium1 = Medium(0, "luki", "", "", 0, 0, 1024, TYPE_GIFS, false, 0)
        val medium2 = Medium(0, "pato", "", "", 0, 0, 2048, TYPE_GIFS, false, 0)

        assertEquals(medium1.getBubbleText(SORT_BY_NAME), medium1.name, "Bubble should be `luki`")
        assertEquals(medium2.getBubbleText(SORT_BY_NAME), medium2.name, "First should be `pato`")

        assertEquals(medium1.getBubbleText(SORT_BY_SIZE), medium1.size.formatSize(), "Bubble should be `${medium1.size.formatSize()}`")
        assertEquals(medium2.getBubbleText(SORT_BY_SIZE), medium2.size.formatSize(), "First should be `${medium2.size.formatSize()}`")
    }

    @Test
    fun isFavouriteTest(){
        val isFavorite = Directory(0, "favorites", "", "", 0, 0, 0, 0, 0, 0)
        val nonFavorite = Directory(0, "downloads", "", "", 0, 0, 0, 0, 0, 0)

        assertTrue(isFavorite.areFavorites(), "Directory should be `favorites`")
        assertFalse(nonFavorite.areFavorites(), "Directory should be `downloads`")
    }

}