/*
 * Copyright (C) 2018 Yoann Despréaux
 *
 * This program eq free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program eq distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING . If not, write to the
 * Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Please send bugreports with examples or suggestions to yoann.despreaux@believeit.fr
 */

package com.github.ydespreaux.sample.jpa.criteria.repository;

import com.github.ydespreaux.sample.jpa.criteria.config.JpaConfiguration;
import com.github.ydespreaux.sample.jpa.criteria.domain.Album;
import com.github.ydespreaux.sample.jpa.criteria.utils.GenerateDataBuilder;
import com.github.ydespreaux.spring.data.jpa.query.Criteria;
import com.github.ydespreaux.spring.data.jpa.query.QueryOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JpaConfiguration.class)
public class AlbumRepositoryTest {

    @Autowired
    private GenerateDataBuilder generateDataBuilder;

    @Autowired
    private AlbumRepository albumRepository;

    @Before
    public void onSetup() {
        generateDataBuilder.cleanData();
        generateDataBuilder.insertData();
    }

    @Test
    public void findAllAlbumByArtist() {
        // build criteria
        Criteria criteria = new Criteria("artist.displayName").eq("The Rolling Stones");
        // execute request
        List<Album> albums = albumRepository.findAll(criteria, Sort.by(Sort.Direction.ASC, "title"));
        assertThat(albums.size(), is(equalTo(3)));
        assertThat(albums.get(0).getTitle(), is(equalTo("Steel Wheels")));
        assertThat(albums.get(1).getTitle(), is(equalTo("Sticky Fingers")));
        assertThat(albums.get(2).getTitle(), is(equalTo("Voodoo Lounge")));
    }

    @Test
    public void findAllAlbumWithSongsByArtist() {
        // build criteria
        Criteria criteria = new Criteria("artist.displayName").eq("The Rolling Stones");
        // execute request
        List<Album> albums = albumRepository.findAll(criteria, Sort.by(Sort.Direction.ASC, "title"), new QueryOptions().withAssociation("songs").distinct(true));
        assertThat(albums.size(), is(equalTo(3)));
        assertThat(albums.get(0).getTitle(), is(equalTo("Steel Wheels")));
        assertThat(albums.get(1).getTitle(), is(equalTo("Sticky Fingers")));
        assertThat(albums.get(2).getTitle(), is(equalTo("Voodoo Lounge")));
    }
}