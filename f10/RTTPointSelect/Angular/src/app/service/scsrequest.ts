import { UUID } from 'angular2-uuid';

export class ScsPathInfo {
    pid: string;
    id: string;
    ts: number;
};

export class ScsRESTRequest {

    /*
     * APIs are grouped in component. This parameter is the name of the component
     * you want to call
     */
    component: string;

    /*
     * Name of API in the component
     */
    request: string;

    /*
     * Contains the name of the user or software calling the API
     */
    source: string;

    /*
     * timestamp when request is send in milliseconds
     */
    requestTimeMS: number;

    /*
     * Unique ID of request to be used when there are several response for the same request.
     *  (e.g. subscription to dbpoller) or for asynchronous call.
     *  If uuid is not in request one will be created in response
     */
    uuid: string;

    /*
     * If the request can generate many responses, the response will be send to this callback.
     *  This is used for subscription (DbmPoller) or multi step request (running a SOC)
     */
    client_callback: string;

    /*
     * Use to trace request path
     */
    path: ScsPathInfo[];

    /*
     * Contains a JSON object with request parameters
     * This specific to every call.
     * Server will return a message with information about parameter in case of error
     */
    parameters: any;

    public constructor(comp: string, req: string, src: string) {
        this.uuid = UUID.UUID();
        this.requestTimeMS = Date.now();
        this.component = comp;
        this.request = req;
        this.source = src;
        this.path = new Array();
        const w = window;

        const pi = new ScsPathInfo();
        pi.pid = 'angular2';
        pi.id = UUID.UUID();
        pi.ts = this.requestTimeMS;
        this.path.push(pi);
    }
};

export class ScsResponse {
    /*
     * APIs are grouped in component. This parameter is the name of the component
     * you want to call
     */
    component: string;

    /*
     * Name of API in the component
     */
    request: string;

    /*
     * Contains the name of the user or software calling the API
     */
    source: string;

    /*
     * timestamp when request is send in milliseconds
     */
    requestTimeMS: number;

    /*
     * errorCode returned by generic code or component specific code.
     * 0 means no error.
     */
    errorCode: number;

    /*
     * error message for humanbeing
     */
    message: string;

    /*
     * timestamp when response is sent from server to client in milliseconds.
     * Use this field with requestTimeMS and current time to measure performance
     */
    responseTimeMS: number;

    /*
     * Unique ID of request to be used when there are several response for the same request.
     *  (e.g. subscription to dbpoller) or for asynchronous call.
     *  If uuid is not in request one will be created in response
     */
    uuid: string;

    /*
     *
     */
    stop_notif: string;

    /*
     * Use to trace request path
     */
    path: ScsPathInfo[];

    /*
     * Contains a JSON object with request response
     * This specific to every call.
     */
    response: any;
};
