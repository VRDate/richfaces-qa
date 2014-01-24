/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.photoalbum.ftest.webdriver.tests;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.tests.photoalbum.ftest.webdriver.annotations.DoNotLogoutAfter;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.PhotoView;
import org.richfaces.tests.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;
import org.testng.annotations.Test;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAddAndDeleteComment extends AbstractPhotoalbumTest {

    @Test
    @DoNotLogoutAfter
    public void addComment() {
        login();

        DateTime dt = new DateTime();
        DateTimeFormatter pattern = DateTimeFormat.forPattern("MMM d, YYYY");

        // open first photo in album 'Monuments and just buildings'
        page.getLeftPanel().openAlbumInPredefinedShelf("Monuments and just buildings", "Monuments");
        getView(AlbumView.class).getPhotos().get(0).open();
        PhotoView photoView = getView(PhotoView.class);
        PhotoView.CommentsPanel commentPanel = photoView.getCommentPanel();

        // check previous comments
        List<PhotoView.CommentsPanel.Comment> comments = commentPanel.getComments();
        assertEquals(comments.size(), 3);
        comments.get(0).checkAll(JAN_DATE, "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll(JAN_DATE, "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_default.png", "amarkhel");
        comments.get(2).checkAll(JAN_DATE, "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");

        // add comment
        String comment = "new comment";
        commentPanel.addComment(comment);

        // check comments
        comments = commentPanel.getComments();
        assertEquals(comments.size(), 4);
        comments.get(0).checkAll(JAN_DATE, "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll(JAN_DATE, "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_default.png", "amarkhel");
        comments.get(2).checkAll(JAN_DATE, "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");
        comments.get(3).checkAll(dt.toString(pattern), comment, "avatar_default.png", "amarkhel");
        comments.get(3).checkIfUsersComment();

        // move to second image in album and back
        photoView.getImagesScroller().getPreviews().get(1).open();
        photoView.getImagesScroller().getPreviews().get(0).open();

        // check comments again
        comments = commentPanel.getComments();
        assertEquals(comments.size(), 4);
        comments.get(0).checkAll(JAN_DATE, "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll(JAN_DATE, "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_default.png", "amarkhel");
        comments.get(2).checkAll(JAN_DATE, "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");
        comments.get(3).checkAll(dt.toString(pattern), comment, "avatar_default.png", "amarkhel");
        comments.get(3).checkIfUsersComment();
    }

    @Test(dependsOnMethods = "addComment")
    public void deleteComment() {
        PhotoView photoView = getView(PhotoView.class);

        List<PhotoView.CommentsPanel.Comment> comments = photoView.getCommentPanel().getComments();

        // delete
        // does not work without the scrolling
        PhotoalbumUtils.scrollToElement(comments.get(3).getUserImage());
        comments.get(3).delete();

        // check if deleted
        comments = photoView.getCommentPanel().getComments();
        assertEquals(comments.size(), 3);
        comments.get(0).checkAll(JAN_DATE, "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll(JAN_DATE, "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_default.png", "amarkhel");
        comments.get(2).checkAll(JAN_DATE, "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");
    }
}
