<?php namespace App\Library\WpApi;


class WpApi
{
    protected $url;

    function __construct($url = 'http://mag.nkit.co/wp-json/wp/v2/')
    {
        $this->url = $url;
    }

    public function getPosts()
    {
        $response = file_get_contents($this->url . 'posts', false);
//        $posts = json_decode($response);
//
//        $p[sizeof($posts)] = new Post();
        return json_decode($response);
    }

    public function getMedia($id)
    {
        $response = file_get_contents($this->url . 'media/' . $id, false);

        return json_decode($response);
    }

    public function getPostById($id)
    {
        $response = file_get_contents($this->url . 'posts/' . $id, false);
        return json_decode($response);
    }

    public function getPostsSummary()
    {

    }
}

