package com.appacitive.sdk.push;

import java.util.List;

public class FixedSizeImageList {
        public FixedSizeImageList(List<String> images) {
            int index = 0;
            _images = new String[9];
            for (String image : images) {
                if (index == 9)
                    break;
                if (image != null && image.isEmpty() == false)
                    _images[index++] = image;
            }
        }

        private String[] _images;

//        public String[] toArray()
//        {
//
//            return _images.Where(x => string.IsNullOrWhiteSpace(x) == false).ToArray();
//        }

        private void Set(int index, String image) {
            _images[index] = image;
        }

        public String Get(int index) {
            return _images[index];
        }

        public String getImage1() {
            return this.Get(0);
        }

        public void setImage1(String image1) {
            this.Set(0, image1);
        }

        public String getImage2() {
            return this.Get(1);
        }

        public void setImage2(String image2) {
            this.Set(1, image2);
        }

        public String getImage3() {
            return this.Get(2);
        }

        public void setImage3(String image3) {
            this.Set(2, image3);
        }

        public String getImage4() {
            return this.Get(3);
        }

        public void setImage4(String image4) {
            this.Set(3, image4);
        }

        public String getImage5() {
            return this.Get(4);
        }

        public void setImage5(String image5) {
            this.Set(4, image5);
        }

        public String getImage6() {
            return this.Get(5);
        }

        public void setImage6(String image6) {
            this.Set(5, image6);
        }

        public String getImage7() {
            return this.Get(6);
        }

        public void setImage7(String image7) {
            this.Set(6, image7);
        }

        public String getImage8() {
            return this.Get(7);
        }

        public void setImage8(String image8) {
            this.Set(7, image8);
        }

        public String getImage9() {
            return this.Get(8);
        }

        public void setImage9(String image9) {
            this.Set(8, image9);
        }
    }
