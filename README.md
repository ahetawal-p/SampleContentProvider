Sample ContentProvider Project
=================================

I did this project in an attempt to clear my basics for the below mentioned terms and how they
interact with each other

#### `ContentProvider`
`ContentProvider` provide the extra level of abstraction over your data to make it easier to change internally.
Able to re-use the same standard API for accessing data rather than littering your code with low-level access to the database.
Also works well with `SyncAdapters` and `Loaders`

#### `SQLiteOpenHelper`
This the actual `Database accessor` which is used inside `contentprovider` methods
`insert/update/delete/query` to access data from sqlite db.

#### `ContentResolver`
The client api, which is used to access the `contentprovider` from an app. An
app only access the data from storage through `ContentResolver`


#### `Loaders/LoaderManager`
Loader is a class offered by the Android Framework. It loads data
in a background thread and offers a callback interface to allow you to
use that data once it's loaded. We use the Loader together with a database cursor.
`Loaders` are usually a better way to call the **query()** method on the `contentprovider` to keep
 updating `recyclerview` as the data is `inserted/updated` on the provider.


```
The call stack looks like :
App -> ContentResolver.query() -> ContentPorivder.query() -> SQLiteOpenHelper.query() -> SQLite DB

Query() -> Use Loaders if you need automatic notifications for data updates.

Insert/Update/Delete -> Use ContentResolver methods for these operations
```

> **Note:** Query() can also be used with Resolver method for one time uses. But if you are poupluating
recyclerviews and need auto updates use loaders.



#### `AsyncQueryHandler`
While `Loaders` take care of fetching data on a background thread and not blocking the main thread.
One should try to perform other operations like `insert` on a background thread too. `AsynQueryHandler`
provides a mechanism to perform these on a different thread and provide a callback on the the main thread
once the operation is completed.



#### `RecyclerView`





