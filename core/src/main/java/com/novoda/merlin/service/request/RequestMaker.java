package com.novoda.merlin.service.request;

import com.novoda.merlin.Endpoint;

interface RequestMaker {
    Request head(Endpoint endpoint);
}
