package stas.batura.stepteacker.data

import stas.batura.stepteacker.data.room.StepsDao
import javax.inject.Inject

interface Repository: StepsDao {

}


class RepositoryImpl @Inject constructor(): Repository {


}