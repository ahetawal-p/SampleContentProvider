Sample ContentProvider Project
=================================

### Motivation
I did this project in an attempt to clear my basics for the below mentioned terms and how they
interact with each other.
The app contains a custom `ContentProvider` to store data entries to from user.
The table just has columns _ID|Name
Uses a `RecyclerView` to display the data from the `ContentProvider` using `Loaders`. Also has couple of buttons to
be used for update/delete data from the `ContentProvider` using `SQLiteOpenHelper` and `ContentResolver`.



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

App -> ContentResolver.query() -> ContentProvider.query() -> SQLiteOpenHelper.query() -> SQLite DB


Query() -> Use Loaders if you need automatic notifications for data updates.
Insert/Update/Delete -> Use ContentResolver methods for these operations
```

> **Note:** Query() can also be used with Resolver method for one time uses. But if you are populating
recyclerviews and need auto updates use loaders.



#### `AsyncQueryHandler`
While `Loaders` take care of fetching data on a background thread and not blocking the main thread.
One should try to perform other operations like `insert` on a background thread too. `AsynQueryHandler`
provides a mechanism to perform these on a different thread and provide a callback on the the main thread
once the operation is completed.



#### `RecyclerView`
Used for displaying the data from the `ContentProvider`. Its a replacement for the older `ListViews`.
I have used `Loaders` along with `RecyclerView` to get automatic updates on UI, if data is added/updated/deleted
on the `ContentProvider`.
> **Note:** Had to integrate some of the Loader functionality in the RecyclerView adapter for it to react accordingly
when it receives datachange notifications from the loader, so that the adapter can update the view/rows accordingly.
See: [DataViewerAdapter.java](https://github.com/ahetawal/SampleContentProvider/blob/master/app/src/main/java/com/sampleapp/adapter/DataViewerAdapter.java) for changes.

