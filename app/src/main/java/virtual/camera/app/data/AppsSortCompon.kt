package virtual.camera.app.data

import android.content.pm.ApplicationInfo

class AppsSortComparator(private val sortedList: List<String>) : Comparator<ApplicationInfo> {
    override fun compare(o1: ApplicationInfo?, o2: ApplicationInfo?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }

        val first = sortedList.indexOf(o1.packageName)
        val second = sortedList.indexOf(o2.packageName)
        return first - second

    }
}