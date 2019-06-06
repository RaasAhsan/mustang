
// 0 is stdin
// 1 is stdout
// 2 is stderr

fn main() {
    unsafe {
        let epoll_fd = libc::epoll_create1(0);
        if epoll_fd == -1 {
            panic!("Failed to create epoll instance");
        }

        let mut epoll_event = libc::epoll_event {
            events: (libc::EPOLLIN | libc::EPOLLET) as u32,
            u64: 0
        };

        if libc::epoll_ctl(epoll_fd, libc::EPOLL_CTL_ADD, 0, &mut epoll_event) == -1 {
            panic!("Failed to add file descriptor to epoll instance.");
        }

        let event = libc::epoll_event {
            events: 0,
            u64: 0
        };

        let mut events: [libc::epoll_event; 4] = [event; 4];

        loop {
            let nfds = libc::epoll_wait(epoll_fd, events.as_mut_ptr(), 4, -1);
            if nfds == -1 {
                panic!("Failed to epoll wait.");
            }

            let mut data: [u8; 16] = [0; 16];

            libc::read(0, data.as_mut_ptr() as *mut libc::c_void, 16);

            println!("Got {}", nfds);
            println!("Data: {:?}", data);
        }
    }
    println!("Hello, world!");
}
