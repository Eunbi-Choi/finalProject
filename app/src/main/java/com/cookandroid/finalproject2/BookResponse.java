package com.cookandroid.finalproject2;

import java.util.List;

public class BookResponse {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private VolumeInfo volumeInfo;

        public VolumeInfo getVolumeInfo() {
            return volumeInfo;
        }

        public void setVolumeInfo(VolumeInfo volumeInfo) {
            this.volumeInfo = volumeInfo;
        }

        public static class VolumeInfo {
            private String title;
            private List<String> authors;
            private ImageLinks imageLinks;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<String> getAuthors() {
                return authors;
            }

            public void setAuthors(List<String> authors) {
                this.authors = authors;
            }

            public ImageLinks getImageLinks() {
                return imageLinks;
            }

            public void setImageLinks(ImageLinks imageLinks) {
                this.imageLinks = imageLinks;
            }

            public static class ImageLinks {
                private String thumbnail;

                public String getThumbnail() {
                    return thumbnail;
                }

                public void setThumbnail(String thumbnail) {
                    this.thumbnail = thumbnail;
                }
            }
        }
    }
}