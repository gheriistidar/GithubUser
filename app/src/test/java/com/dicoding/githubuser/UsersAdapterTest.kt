import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.ui.UsersAdapter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UsersAdapterTest {

    private val oldItem = ItemsItem(id = 1, login = "user1", avatarUrl = "url1")
    private val newItemSameAsOld = ItemsItem(id = 1, login = "user1", avatarUrl = "url1")
    private val newItemDifferentFromOld = ItemsItem(id = 2, login = "user2", avatarUrl = "url2")

    @Test
    fun testAreItemsTheSame() {
        assertTrue(UsersAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItemSameAsOld))
        assertFalse(UsersAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItemDifferentFromOld))
    }

    @Test
    fun testAreContentsTheSame() {
        assertTrue(UsersAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItemSameAsOld))
        assertFalse(UsersAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItemDifferentFromOld))
    }
}
