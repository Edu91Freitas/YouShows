package org.michaelbel.youshows.rest.model;

import com.google.gson.annotations.SerializedName;

import org.michaelbel.youshows.rest.TmdbObject;

import java.io.Serializable;

/**
 * Date: 02 APR 2018
 * Time: 00:24 MSK
 *
 * @author Michael Bel
 */

public class Genre extends TmdbObject implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;
}