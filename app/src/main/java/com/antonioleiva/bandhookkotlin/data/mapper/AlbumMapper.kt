package com.antonioleiva.bandhookkotlin.data.mapper

import com.antonioleiva.bandhookkotlin.data.lastfm.model.LastFmAlbum
import com.antonioleiva.bandhookkotlin.data.lastfm.model.LastFmAlbumDetail
import com.antonioleiva.bandhookkotlin.domain.entity.Album
import com.antonioleiva.bandhookkotlin.domain.entity.Artist

/**
 * @author tpom6oh@gmail.com
 *
 * 03/07/16.
 */

class AlbumMapper(val artistMapper: ArtistMapper = ArtistMapper(), val imageMapper: ImageMapper = ImageMapper(),
                  val trackMapper: TrackMapper = TrackMapper()) {

    fun transform(albums: List<LastFmAlbum>): List<Album> {
        return albums.filter { albumHasQualityInfo(it) }.mapNotNull { transform(it) }
    }

    private fun albumHasQualityInfo(album: LastFmAlbum): Boolean {
        return !isAlbumMbidEmpty(album) && album.images.size > 0
    }

    private fun isAlbumMbidEmpty(album: LastFmAlbum): Boolean {
        return album.mbid?.isEmpty() ?: true
    }

    fun transform(album: LastFmAlbumDetail): Album? {
        if (album.mbid != null) {
            return Album(
                    album.mbid,
                    album.name,
                    Artist("", album.artist),
                    imageMapper.getMainImageUrl(album.images),
                    trackMapper.transform(album.tracks.tracks))
        } else {
            return null
        }
    }

    fun transform(album: LastFmAlbum): Album? {
        if (album.mbid != null) {
            return Album(
                    album.mbid,
                    album.name,
                    artistMapper.transform(album.artist),
                    imageMapper.getMainImageUrl(album.images),
                    trackMapper.transform(album.tracks?.tracks))
        } else {
            return null
        }
    }
}