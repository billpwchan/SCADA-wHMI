#ifndef __RED_API_H__
#define __RED_API_H__

#ifndef WIN32
    #define RED_API
#else
    #ifdef RED_EXPORT
        #define RED_API __declspec(dllexport)
    #else
        #define RED_API __declspec(dllimport)
    #endif
#endif

#endif /* include guard */
